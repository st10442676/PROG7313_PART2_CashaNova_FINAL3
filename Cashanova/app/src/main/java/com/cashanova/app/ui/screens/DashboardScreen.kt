package com.cashanova.app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.BottomNavBar
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Phillips, B., Stewart, C. and Marsicano, K. 2019. Android Programming: The Big Nerd Ranch Guide. 4th ed. Atlanta: Big Nerd Ranch.

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val today = LocalDate.now()
    val monthYear = today.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    val startDate = "$monthYear-01"
    val endDate = "$monthYear-31"

    val totalSpent by viewModel.getTotalSpent(startDate, endDate).collectAsState(initial = 0.0)
    val budget by viewModel.getBudgetForMonth(monthYear).collectAsState(initial = null)
    val expenses by viewModel.getExpensesByPeriod(startDate, endDate).collectAsState(initial = emptyList())
    val categories by viewModel.getCategories().collectAsState(initial = emptyList())
    val username = viewModel.sessionManager.getUsername()

    val spent = totalSpent ?: 0.0
    val goal = budget?.totalGoal ?: 0.0
    val progress = if (goal > 0) (spent / goal).coerceIn(0.0, 1.0) else 0.0

    Scaffold(
        containerColor = Black.copy(alpha = 0.08f),
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Routes.DASHBOARD)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Welcome back,", color = TextGray, fontSize = 14.sp)
                    Text(
                        username.uppercase(),
                        color = Gold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                IconButton(onClick = { navController.navigate(Routes.INBOX) }) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Inbox", tint = Gold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Spending Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF1A1A00), Color(0xFF2A2000)))
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text("MONTHLY SPENDING", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "R ${String.format("%.2f", spent)}",
                        color = Gold,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                    if (goal > 0) {
                        Text("of R ${String.format("%.2f", goal)} budget", color = TextGray, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progress.toFloat() },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = if (progress > 0.9) ErrorRed else Gold,
                            trackColor = DarkGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${(progress * 100).toInt()}% used",
                            color = if (progress > 0.9) ErrorRed else TextGray,
                            fontSize = 12.sp
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No budget set",
                            color = TextGray,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { navController.navigate(Routes.SET_BUDGET_GOAL) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions
            Text("QUICK ACTIONS", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard("Add Expense", Icons.Filled.Add, Modifier.weight(1f)) {
                    navController.navigate(Routes.CREATE_EXPENSE)
                }
                QuickActionCard("Set Budget", Icons.Filled.BarChart, Modifier.weight(1f)) {
                    navController.navigate(Routes.SET_BUDGET_GOAL)
                }
                QuickActionCard("Categories", Icons.Filled.Category, Modifier.weight(1f)) {
                    navController.navigate(Routes.CREATE_CATEGORY)
                }
                QuickActionCard("Search", Icons.Filled.Search, Modifier.weight(1f)) {
                    navController.navigate(Routes.SEARCH)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Spending by Category
            if (categories.isNotEmpty()) {
                Text("SPENDING BY CATEGORY", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(10.dp))
                categories.take(4).forEach { cat ->
                    val catTotal by viewModel.getTotalByCategory(cat.id, startDate, endDate)
                        .collectAsState(initial = 0.0)
                    val catSpent = catTotal ?: 0.0
                    val catProgress = if (cat.monthlyLimit > 0) (catSpent / cat.monthlyLimit).coerceIn(0.0, 1.0) else 0.0

                    CategoryProgressRow(
                        name = cat.name,
                        spent = catSpent,
                        limit = cat.monthlyLimit,
                        progress = catProgress.toFloat(),
                        color = try { Color(android.graphics.Color.parseColor(cat.colorHex)) } catch (e: Exception) { Gold }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Expenses
            if (expenses.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("RECENT EXPENSES", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
                    Text(
                        "See all →",
                        color = Gold,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { navController.navigate(Routes.EXPENSES) }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                expenses.take(5).forEach { expense ->
                    val cat = categories.find { it.id == expense.categoryId }
                    RecentExpenseRow(
                        description = expense.description,
                        amount = expense.amount,
                        date = expense.date,
                        categoryName = cat?.name ?: "Uncategorised",
                        categoryColor = try {
                            Color(android.graphics.Color.parseColor(cat?.colorHex ?: "#FFD700"))
                        } catch (e: Exception) { Gold }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickActionCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BlackCard)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = Gold, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun CategoryProgressRow(name: String, spent: Double, limit: Double, progress: Float, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(
                "R ${String.format("%.2f", spent)}${if (limit > 0) " / R ${String.format("%.2f", limit)}" else ""}",
                color = if (progress > 0.9f) ErrorRed else TextGray,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = if (progress > 0.9f) ErrorRed else color,
            trackColor = DarkGray
        )
    }
}

@Composable
fun RecentExpenseRow(description: String, amount: Double, date: String, categoryName: String, categoryColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BlackCard)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(categoryColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(description, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("$categoryName • $date", color = TextGray, fontSize = 11.sp)
            }
        }
        Text(
            "- R ${String.format("%.2f", amount)}",
            color = ErrorRed,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}