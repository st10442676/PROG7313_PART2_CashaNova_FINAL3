package com.cashanova.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"])]
)
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val monthYear: String,
    val totalGoal: Double,
    val minimumGoal: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val fixedExpenses: Double = 0.0
)
//Griffiths, D. and Griffiths, D. 2021. Head First Android Development: A Learner's Guide to Building Android Apps with Kotlin. 3rd ed. Sebastopol: O'Reilly Media.