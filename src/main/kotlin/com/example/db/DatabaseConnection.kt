package com.example.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:mysql://l0ebsc9jituxzmts.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/x6rlku7zqgt0s123",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "xrdx85q4se9m1r8y",
        password = "o4dcu2pafe3wvysl"
    )
}