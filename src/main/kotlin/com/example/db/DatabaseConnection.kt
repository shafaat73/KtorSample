package com.example.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:mysql://au77784bkjx6ipju.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/wync6zbooqh3z3oa",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "lfyjesz1kptippj3",
        password = "oogeli10zmvdvsqr"
    )
}