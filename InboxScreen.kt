package com.cashanova.app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.BottomNavBar
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InboxScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val today = LocalDate.now()
    val monthYear = today.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    val startDate = "$monthYear-01"
    val endDate = "$monthYear-31"

    val totalSpent by viewModel.getTotalSpent(startDate, endDate).collectAsState(initial = 0.0)
    val budget by viewModel.getBudgetForMonth(monthYear).collectAsState(initial = null)
    val categories by viewModel.getCategories().collectAsState(initial = emptyList())
    val expenses by viewModel.getExpensesByPeriod(startDate, endDate).collectAsState(initial = emptyList())

    val spent = totalSpent ?: 0.0
    val goal = budget?.totalGoal ?: 0.0
    val progress = if (goal > 0) spent / goal else 0.0

    // Generate smart notifications
    val notifications = buildList {
        if (goal > 0 && progress >= 1.0) {
            add(InboxNotification(Icons.Filled.Warning, "Budget Exceeded!", "You've gone over your R ${String.format(Locale.getDefault(), "%.2f", goal)} budget this month.", ErrorRed))
        } else if (goal > 0 && progress >= 0.8) {
            add(InboxNotification(Icons.Filled.Notifications, "Budget Alert", "You've used ${(progress * 100).toInt()}% of your budget. R ${String.format(Locale.getDefault(), "%.2f", goal - spent)} remaining.", WarningOrange))
        }
        if (expenses.size >= 10) {
            add(InboxNotification(Icons.Filled.TrendingUp, "Spending Insight", "You have ${expenses.size} transactions this month. Great job tracking!", Gold))
        }
        if (categories.isEmpty()) {
            add(InboxNotification(Icons.Filled.Category, "Setup Tip", "Create expense categories to better organise your spending.", Gold))
        }
        if (goal == 0.0) {
            add(InboxNotification(Icons.Filled.Flag, "Set Your Budget", "You haven't set a budget goal yet. Tap to set one now!", Gold))
        }
        add(InboxNotification(Icons.Filled.Lightbulb, "Daily Tip", "Review your expenses weekly to stay on track with your financial goals.", TextGray))
        add(InboxNotification(Icons.Filled.Star, "Welcome to Cashanova", "Track every rand you spend to build better financial habits.", Gold))
    }

    Scaffold(
        containerColor = Black.copy(alpha = 0.08f),
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Routes.INBOX) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("INBOX", color = Gold, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("${notifications.size} notifications", color = TextGray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick links
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                InboxQuickLink("Budget", Icons.Filled.BarChart, Modifier.weight(1f)) {
                    navController.navigate(Routes.BUDGET)
                }
                InboxQuickLink("Logout", Icons.Filled.Logout, Modifier.weight(1f)) {
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("NOTIFICATIONS", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(notifications.size) { index ->
                    val (icon, title, message, color) = notifications[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BlackCard)
                            .clickable {
                                when (title) {
                                    "Set Your Budget" -> navController.navigate(Routes.SET_BUDGET_GOAL)
                                    "Setup Tip" -> navController.navigate(Routes.CREATE_CATEGORY)
                                    else -> {}
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color = color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(message, color = TextGray, fontSize = 13.sp, lineHeight = 18.sp)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

data class InboxNotification(
    val icon: ImageVector,
    val title: String,
    val message: String,
    val color: Color
)

@Composable
fun InboxQuickLink(label: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BlackCard)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label, tint = Gold, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

//Phillips, B., Stewart, C. and Marsicano, K. 2019. Android Programming: The Big Nerd Ranch Guide. 4th ed. Atlanta: Big Nerd Ranch.
