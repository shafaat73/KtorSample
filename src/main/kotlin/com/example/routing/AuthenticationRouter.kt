package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.UserEntity
import com.example.models.NoteResponse
import com.example.models.User
import com.example.models.UserCredentials
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.authenticationRoutes(){
    val db = DatabaseConnection.database
    val tokenManger = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    routing {
        post("/register"){
            val data = call.receive<UserCredentials>()
            val username = data.username.toString().lowercase()
            val password = data.hashPassword()

            val user = db.from(UserEntity).select()
                .where { UserEntity.username eq username }
                .map { it[UserEntity.username] }
                .firstOrNull()

            if (user != null){
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "Same user exist", data = null))
                return@post
            }

            val insertData = db.insert(UserEntity){
                set(UserEntity.username, username)
                set(UserEntity.password, password)
            }

            if (insertData == 1){
                call.respond(HttpStatusCode.OK, NoteResponse(isSuccess = true, data = data))
            }else{
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "Something went wrong", data = null))
            }
        }


        post("/login"){
            val data = call.receive<UserCredentials>()
            val username = data.username.toString().lowercase()
            val password = data.password

            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.username eq username }
                .map {
                    val id = it[UserEntity.id]
                    val username = it[UserEntity.username]
                    val password = it[UserEntity.password]
                    User(id,username,password)
                }.firstOrNull()


            if (user == null){
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "Invalid Username or password", data = null))
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)
            if (!doesPasswordMatch){
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "Invalid Username or password", data = null))
                return@post
            }

            val token = tokenManger.generateJWTToken(user)
            call.respond(HttpStatusCode.OK, NoteResponse(isSuccess = true, data = token))
        }

        authenticate {
            get("/me") {
                val data = call.principal<JWTPrincipal>()
                val username = data?.payload?.getClaim("username")?.asString()
                val userID = data?.payload?.getClaim("userId")?.asInt()
                call.respondText("Hello, $username with id $userID")
            }
        }
    }
}