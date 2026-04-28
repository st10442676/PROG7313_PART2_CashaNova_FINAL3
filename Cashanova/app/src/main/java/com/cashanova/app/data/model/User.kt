package com.cashanova.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String,
    val fullName: String,
    val surname: String,
    val email: String,
    val contactNumber: String,
    val dateOfBirth: String
)

//Griffiths, D. and Griffiths, D. 2021. Head First Android Development: A Learner's Guide to Building Android Apps with Kotlin. 3rd ed. Sebastopol: O'Reilly Media.