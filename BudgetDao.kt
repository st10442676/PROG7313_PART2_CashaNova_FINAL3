package com.cashanova.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cashanova.app.data.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId AND monthYear = :monthYear LIMIT 1")
    fun getBudgetForMonth(userId: Int, monthYear: String): Flow<Budget?>

    @Query("SELECT * FROM budgets WHERE userId = :userId ORDER BY monthYear DESC")
    fun getAllBudgets(userId: Int): Flow<List<Budget>>
}