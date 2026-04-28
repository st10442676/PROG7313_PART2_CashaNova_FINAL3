package com.cashanova.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cashanova.app.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        AND date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getExpensesByPeriod(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Expense>>

    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        AND categoryId = :categoryId 
        AND date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getExpensesByCategoryAndPeriod(
        userId: Int,
        categoryId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Expense>>

    @Query("""
        SELECT SUM(amount) FROM expenses 
        WHERE userId = :userId 
        AND categoryId = :categoryId 
        AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalByCategory(
        userId: Int,
        categoryId: Int,
        startDate: String,
        endDate: String
    ): Flow<Double?>

    @Query("""
        SELECT SUM(amount) FROM expenses 
        WHERE userId = :userId 
        AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalSpent(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<Double?>

    @Query("""
        SELECT * FROM expenses
        WHERE userId = :userId
        AND description LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchExpenses(userId: Int, query: String): Flow<List<Expense>>
}