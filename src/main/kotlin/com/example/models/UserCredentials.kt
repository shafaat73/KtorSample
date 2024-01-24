package com.example.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserCredentials(
    val username : String,
    var password : String
){
    fun hashPassword() : String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }
}

data class User(
    val id : Int?,
    val username : String?,
    val password: String?
)