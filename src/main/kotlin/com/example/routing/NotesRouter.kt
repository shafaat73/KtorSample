package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.NoteEntity
import com.example.models.Note
import com.example.models.NoteRequest
import com.example.models.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.notesRoutes() {
    val db = DatabaseConnection.database

    routing {
        get("/notes") {
            val notes = db.from(NoteEntity).select().map {
                Note(it[NoteEntity.id], it[NoteEntity.note])
            }

            call.respond(notes)
        }

        post("/notes") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(NoteEntity) {
                set(it.note, request.note)
            }

            if (result == 1) {
                call.respond(HttpStatusCode.OK, NoteResponse(isSuccess = true, data = "Success"))
            } else {
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "No Data Found", data = null))
            }
        }

        get("note/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val result = db.from(NoteEntity).select().where {
                NoteEntity.id eq id
            }.map {
                Note(it[NoteEntity.id], it[NoteEntity.note])
            }.firstOrNull()

            if (result != null){
                call.respond(HttpStatusCode.OK,NoteResponse(isSuccess = true, data = result))
            }else{
                call.respond(HttpStatusCode.NotFound, NoteResponse(isSuccess = false, error = "Not Found", data = null))
            }
        }

        put("/note/{id}"){
            val id = call.parameters["id"]?.toInt() ?: -1
            val note = call.receive<NoteRequest>()

            val result = db.update(NoteEntity){
                set(it.note, note.note)
                where {
                    it.id eq id
                }
            }

            if (result == 1) {
                call.respond(HttpStatusCode.OK, NoteResponse(isSuccess = true, data = "Success"))
            } else {
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "No Data Found", data = null))
            }
        }

        delete("/note/{id}"){
            val id = call.parameters["id"]?.toInt() ?: -1

            val result = db.delete(NoteEntity){
                    it.id eq id
            }

            if (result == 1) {
                call.respond(HttpStatusCode.OK, NoteResponse(isSuccess = true, data = "Success"))
            } else {
                call.respond(HttpStatusCode.BadRequest, NoteResponse(isSuccess = false, error = "No Data Found", data = null))
            }
        }
    }
}