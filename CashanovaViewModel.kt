package com.cashanova.app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cashanova.app.data.AppDatabase
import com.cashanova.app.data.SessionManager
import com.cashanova.app.data.model.Budget
import com.cashanova.app.data.model.Category
import com.cashanova.app.data.model.Expense
import com.cashanova.app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class CashanovaViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "CashanovaViewModel"

    // Database & Session
    private val db             = AppDatabase.getDatabase(application)
    private val userDao        = db.userDao()
    private val categoryDao    = db.categoryDao()
    private val expenseDao     = db.expenseDao()
    private val budgetDao      = db.budgetDao()
    val sessionManager         = SessionManager(application)

    // ── Auth State ────────────────────────────────────────────────────────────
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError

    private val _isLoggedIn = MutableStateFlow(sessionManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // ── Register ──────────────────────────────────────────────────────────────
    fun register(
        fullName: String,
        surname: String,
        email: String,
        contactNumber: String,
        dateOfBirth: String,
        username: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Attempting registration for username: $username")

            // Validation
            if (fullName.isBlank() || surname.isBlank() || username.isBlank() || password.isBlank()) {
                _registerError.value = "Please fill in all required fields"
                return@launch
            }
            if (password != confirmPassword) {
                _registerError.value = "Passwords do not match"
                return@launch
            }
            if (password.length < 6) {
                _registerError.value = "Password must be at least 6 characters"
                return@launch
            }
            val existing = userDao.getUserByUsername(username)
            if (existing != null) {
                _registerError.value = "Username already taken"
                return@launch
            }

            val user = User(
                fullName      = fullName,
                surname       = surname,
                email         = email,
                contactNumber = contactNumber,
                dateOfBirth   = dateOfBirth,
                username      = username,
                password      = password
            )
            val id = userDao.insertUser(user)
            Log.d(TAG, "User registered with id: $id")
            sessionManager.saveSession(id.toInt(), username)
            _isLoggedIn.value = true
            onSuccess()
        }
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            Log.d(TAG, "Attempting login for username: $username")
            if (username.isBlank() || password.isBlank()) {
                _loginError.value = "Please enter username and password"
                return@launch
            }
            val user = userDao.login(username, password)
            if (user != null) {
                Log.d(TAG, "Login successful for userId: ${user.id}")
                sessionManager.saveSession(user.id, user.username)
                _isLoggedIn.value = true
                _loginError.value = null
                onSuccess()
            } else {
                Log.w(TAG, "Login failed - invalid credentials")
                _loginError.value = "Invalid username or password"
            }
        }
    }

    // ── Logout ────────────────────────────────────────────────────────────────
    fun logout() {
        Log.d(TAG, "User logged out")
        sessionManager.clearSession()
        _isLoggedIn.value = false
    }

    fun clearLoginError()    { _loginError.value    = null }
    fun clearRegisterError() { _registerError.value = null }

    // ── Categories ────────────────────────────────────────────────────────────
    fun getCategories(): Flow<List<Category>> {
        val userId = sessionManager.getUserId()
        return if (userId == -1) flowOf(emptyList())
        else categoryDao.getCategoriesByUser(userId)
    }

    fun createCategory(name: String, iconName: String, colorHex: String) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == -1) return@launch
            Log.d(TAG, "Creating category: $name for userId: $userId")
            categoryDao.insertCategory(
                Category(
                    userId    = userId,
                    name      = name,
                    iconName  = iconName,
                    colorHex  = colorHex
                )
            )
        }
    }

    fun updateCategoryLimit(category: Category, limit: Double) {
        viewModelScope.launch {
            Log.d(TAG, "Updating limit for category: ${category.name} to $limit")
            categoryDao.updateCategory(category.copy(monthlyLimit = limit))
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            Log.d(TAG, "Deleting category: ${category.name}")
            categoryDao.deleteCategory(category)
        }
    }

    // ── Expenses ──────────────────────────────────────────────────────────────
    fun getExpensesByPeriod(startDate: String, endDate: String): Flow<List<Expense>> {
        val userId = sessionManager.getUserId()
        return if (userId == -1) flowOf(emptyList())
        else expenseDao.getExpensesByPeriod(userId, startDate, endDate)
    }

    fun getTotalSpent(startDate: String, endDate: String): Flow<Double?> {
        val userId = sessionManager.getUserId()
        return if (userId == -1) flowOf(null)
        else expenseDao.getTotalSpent(userId, startDate, endDate)
    }

    fun getTotalByCategory(
        categoryId: Int,
        startDate: String,
        endDate: String
    ): Flow<Double?> {
        val userId = sessionManager.getUserId()
        return if (userId == -1) flowOf(null)
        else expenseDao.getTotalByCategory(userId, categoryId, startDate, endDate)
    }

    fun searchExpenses(query: String): Flow<List<Expense>> {
        val userId = sessionManager.getUserId()
        return if (userId == -1) flowOf(emptyList())
        else expenseDao.searchExpenses(userId, query)
    }

    fun createExpense(
        categoryId: Int,
        amount: Double,
        description: String,
        date: String,
        startTime: String = "",
        endTime: String = "",
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == -1) return@launch
            Log.d(TAG, "Creating expense: $description, amount: $amount, date: $date")
            expenseDao.insertExpense(
                Expense(
                    userId      = userId,
                    categoryId  = categoryId,
                    amount      = amount,
                    description = description,
                    date        = date,
                    startTime   = startTime,
                    endTime     = endTime,
                    photoPath   = photoPath
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            Log.d(TAG, "Deleting expense id: ${expense.id}")
            expenseDao.deleteExpense(expense)
        }
    }

    // ── Budget ────────────────────────────────────────────────────────────────
    fun getBudgetForMonth(monthYear: String): Flow<Budget?> {
        val userId = sessionManager.getUserId()
        return if (userId == -1) flowOf(null)
        else budgetDao.getBudgetForMonth(userId, monthYear)
    }

    fun saveBudget(
        monthYear: String,
        totalGoal: Double,
        minimumGoal: Double = 0.0,
        monthlyIncome: Double = 0.0,
        fixedExpenses: Double = 0.0
    ) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == -1) return@launch
            Log.d(TAG, "Saving budget for $monthYear: goal=$totalGoal")
            budgetDao.insertOrUpdateBudget(
                Budget(
                    userId        = userId,
                    monthYear     = monthYear,
                    totalGoal     = totalGoal,
                    minimumGoal   = minimumGoal,
                    monthlyIncome = monthlyIncome,
                    fixedExpenses = fixedExpenses
                )
            )
        }
    }
}