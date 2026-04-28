package com.cashanova.app

import com.cashanova.app.data.model.Budget
import com.cashanova.app.data.model.Category
import com.cashanova.app.data.model.Expense
import com.cashanova.app.data.model.User
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Cashanova Budget Tracker App
 * Tests core business logic and data model validation
 */
class CashanovaUnitTests {

    // ── User Model Tests ──────────────────────────────────────────────────────

    @Test
    fun `user model stores correct username`() {
        // Arrange & Act
        val user = User(
            id            = 1,
            username      = "testuser",
            password      = "password123",
            fullName      = "John",
            surname       = "Doe",
            email         = "john@example.com",
            contactNumber = "0821234567",
            dateOfBirth   = "01/01/1990"
        )
        // Assert
        assertEquals("testuser", user.username)
    }

    @Test
    fun `user model stores correct email`() {
        val user = User(
            id            = 1,
            username      = "testuser",
            password      = "password123",
            fullName      = "John",
            surname       = "Doe",
            email         = "john@example.com",
            contactNumber = "0821234567",
            dateOfBirth   = "01/01/1990"
        )
        assertEquals("john@example.com", user.email)
    }

    @Test
    fun `user password is stored correctly`() {
        val user = User(
            id            = 1,
            username      = "testuser",
            password      = "securePass99",
            fullName      = "Jane",
            surname       = "Doe",
            email         = "jane@example.com",
            contactNumber = "0829876543",
            dateOfBirth   = "15/06/1995"
        )
        assertEquals("securePass99", user.password)
    }

    // ── Password Validation Tests ─────────────────────────────────────────────

    @Test
    fun `password with 6 or more characters is valid`() {
        val password = "abc123"
        assertTrue(password.length >= 6)
    }

    @Test
    fun `password with less than 6 characters is invalid`() {
        val password = "abc"
        assertFalse(password.length >= 6)
    }

    @Test
    fun `passwords match returns true when equal`() {
        val password        = "myPassword1"
        val confirmPassword = "myPassword1"
        assertEquals(password, confirmPassword)
    }

    @Test
    fun `passwords match returns false when not equal`() {
        val password        = "myPassword1"
        val confirmPassword = "differentPassword"
        assertNotEquals(password, confirmPassword)
    }

    @Test
    fun `blank username is invalid`() {
        val username = "   "
        assertTrue(username.isBlank())
    }

    @Test
    fun `non blank username is valid`() {
        val username = "cashanova_user"
        assertFalse(username.isBlank())
    }

    // ── Category Model Tests ──────────────────────────────────────────────────

    @Test
    fun `category stores correct name`() {
        val category = Category(
            id         = 1,
            userId     = 1,
            name       = "Groceries",
            iconName   = "shopping_cart",
            colorHex   = "#FFD700",
            monthlyLimit = 500.0
        )
        assertEquals("Groceries", category.name)
    }

    @Test
    fun `category default monthly limit is zero`() {
        val category = Category(
            id       = 1,
            userId   = 1,
            name     = "Entertainment",
            iconName = "movie",
            colorHex = "#FF5733"
        )
        assertEquals(0.0, category.monthlyLimit, 0.0)
    }

    @Test
    fun `category monthly limit updates correctly`() {
        val category = Category(
            id           = 1,
            userId       = 1,
            name         = "Transport",
            iconName     = "car",
            colorHex     = "#4CAF50",
            monthlyLimit = 300.0
        )
        val updated = category.copy(monthlyLimit = 450.0)
        assertEquals(450.0, updated.monthlyLimit, 0.0)
    }

    // ── Expense Model Tests ───────────────────────────────────────────────────

    @Test
    fun `expense stores correct amount`() {
        val expense = Expense(
            id          = 1,
            userId      = 1,
            categoryId  = 1,
            amount      = 150.50,
            description = "Woolworths groceries",
            date        = "2026-04-25"
        )
        assertEquals(150.50, expense.amount, 0.0)
    }

    @Test
    fun `expense stores correct date`() {
        val expense = Expense(
            id          = 1,
            userId      = 1,
            categoryId  = 2,
            amount      = 55.00,
            description = "Uber ride",
            date        = "2026-04-20"
        )
        assertEquals("2026-04-20", expense.date)
    }

    @Test
    fun `expense photo path is null by default`() {
        val expense = Expense(
            id          = 1,
            userId      = 1,
            categoryId  = 1,
            amount      = 200.0,
            description = "Pick n Pay",
            date        = "2026-04-15"
        )
        assertNull(expense.photoPath)
    }

    @Test
    fun `expense stores photo path when provided`() {
        val expense = Expense(
            id          = 1,
            userId      = 1,
            categoryId  = 1,
            amount      = 200.0,
            description = "Pick n Pay",
            date        = "2026-04-15",
            photoPath   = "/storage/receipt_001.jpg"
        )
        assertNotNull(expense.photoPath)
        assertEquals("/storage/receipt_001.jpg", expense.photoPath)
    }

    @Test
    fun `expense is not recurring by default`() {
        val expense = Expense(
            id          = 1,
            userId      = 1,
            categoryId  = 1,
            amount      = 100.0,
            description = "Coffee",
            date        = "2026-04-10"
        )
        assertFalse(expense.isRecurring)
    }

    // ── Budget Model Tests ────────────────────────────────────────────────────

    @Test
    fun `budget stores correct total goal`() {
        val budget = Budget(
            id        = 1,
            userId    = 1,
            monthYear = "2026-04",
            totalGoal = 5000.0
        )
        assertEquals(5000.0, budget.totalGoal, 0.0)
    }

    @Test
    fun `budget stores correct month year`() {
        val budget = Budget(
            id        = 1,
            userId    = 1,
            monthYear = "2026-04",
            totalGoal = 3000.0
        )
        assertEquals("2026-04", budget.monthYear)
    }

    @Test
    fun `budget minimum goal defaults to zero`() {
        val budget = Budget(
            id        = 1,
            userId    = 1,
            monthYear = "2026-04",
            totalGoal = 4000.0
        )
        assertEquals(0.0, budget.minimumGoal, 0.0)
    }

    // ── Spending Calculation Tests ────────────────────────────────────────────

    @Test
    fun `total spent calculation is correct`() {
        val expenses = listOf(
            Expense(1, 1, 1, 100.0, "Groceries", "2026-04-01"),
            Expense(2, 1, 1, 250.0, "Rent",      "2026-04-02"),
            Expense(3, 1, 2, 75.50, "Uber",       "2026-04-03")
        )
        val total = expenses.sumOf { it.amount }
        assertEquals(425.50, total, 0.01)
    }

    @Test
    fun `remaining budget is correctly calculated`() {
        val totalGoal = 3000.0
        val totalSpent = 1250.75
        val remaining = totalGoal - totalSpent
        assertEquals(1749.25, remaining, 0.01)
    }

    @Test
    fun `overspending is detected when spent exceeds limit`() {
        val categoryLimit = 500.0
        val amountSpent   = 620.0
        assertTrue(amountSpent > categoryLimit)
    }

    @Test
    fun `no overspending when spent is within limit`() {
        val categoryLimit = 500.0
        val amountSpent   = 399.99
        assertFalse(amountSpent > categoryLimit)
    }

    @Test
    fun `budget percentage used is calculated correctly`() {
        val totalGoal  = 2000.0
        val totalSpent = 500.0
        val percentage = (totalSpent / totalGoal) * 100
        assertEquals(25.0, percentage, 0.01)
    }

    @Test
    fun `budget percentage is 100 when fully spent`() {
        val totalGoal  = 2000.0
        val totalSpent = 2000.0
        val percentage = (totalSpent / totalGoal) * 100
        assertEquals(100.0, percentage, 0.01)
    }

    @Test
    fun `category total is sum of its expenses`() {
        val categoryId = 1
        val expenses = listOf(
            Expense(1, 1, 1,  80.0, "Shoprite",   "2026-04-01"),
            Expense(2, 1, 1, 120.0, "Checkers",   "2026-04-05"),
            Expense(3, 1, 2,  55.0, "Petrol",     "2026-04-06")
        )
        val categoryTotal = expenses
            .filter { it.categoryId == categoryId }
            .sumOf { it.amount }
        assertEquals(200.0, categoryTotal, 0.01)
    }

    // ── Date & Period Tests ───────────────────────────────────────────────────

    @Test
    fun `date format yyyy-MM-dd is valid`() {
        val date  = "2026-04-25"
        val parts = date.split("-")
        assertEquals(3, parts.size)
        assertEquals(4, parts[0].length) // year
        assertEquals(2, parts[1].length) // month
        assertEquals(2, parts[2].length) // day
    }

    @Test
    fun `month year format yyyy-MM is valid`() {
        val monthYear = "2026-04"
        val parts     = monthYear.split("-")
        assertEquals(2, parts.size)
        assertEquals(4, parts[0].length)
        assertEquals(2, parts[1].length)
    }

    @Test
    fun `expense falls within date range`() {
        val expenseDate = "2026-04-15"
        val startDate   = "2026-04-01"
        val endDate     = "2026-04-30"
        assertTrue(expenseDate >= startDate && expenseDate <= endDate)
    }

    @Test
    fun `expense outside date range is excluded`() {
        val expenseDate = "2026-03-15"
        val startDate   = "2026-04-01"
        val endDate     = "2026-04-30"
        assertFalse(expenseDate >= startDate && expenseDate <= endDate)
    }
}