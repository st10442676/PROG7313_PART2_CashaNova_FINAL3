package com.cashanova.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cashanova.app.data.dao.BudgetDao
import com.cashanova.app.data.dao.CategoryDao
import com.cashanova.app.data.dao.ExpenseDao
import com.cashanova.app.data.dao.UserDao
import com.cashanova.app.data.model.Budget
import com.cashanova.app.data.model.Category
import com.cashanova.app.data.model.Expense
import com.cashanova.app.data.model.User

// Android Developers. 2024. Room Database. [Online]. Available at: https://developer.android.com/training/data-storage/room

@Database(
    entities = [User::class, Category::class, Expense::class, Budget::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cashanova_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}