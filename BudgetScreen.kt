package com.cashanova.app.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.BottomNavBar
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.components.GoldTextField
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel
import java.text.SimpleDateFormat
import java.util.*

@androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.O)
@Composable
fun BudgetScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val cal = Calendar.getInstance()
    val currentMonthYear = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(cal.time)
    val defaultStart = "$currentMonthYear-01"
    val defaultEnd = "$currentMonthYear-31"

    var startDate by remember { mutableStateOf(defaultStart) }
    var endDate by remember { mutableStateOf(defaultEnd) }
    var showDatePicker by remember { mutableStateOf(false) }
    var tempStart by remember { mutableStateOf(defaultStart) }
    var tempEnd by remember { mutableStateOf(defaultEnd) }

    val budget by viewModel.getBudgetForMonth(currentMonthYear).collectAsState(initial = null)
    val totalSpent by viewModel.getTotalSpent(startDate, endDate).collectAsState(initial = 0.0)
    val categories by viewModel.getCategories().collectAsState(initial = emptyList())

    val spent = totalSpent ?: 0.0
    val goal = budget?.totalGoal ?: 0.0
    val remaining = (goal - spent).coerceAtLeast(0.0)
    val progress = if (goal > 0) (spent / goal).coerceIn(0.0, 1.0) else 0.0

    // ── Date picker dialog ────────────────────────────────────────────────────
    if (showDatePicker) {
        Dialog(onDismissRequest = { showDatePicker = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BlackCard)
                    .padding(20.dp)
            ) {
                Column {
                    Text("SELECT PERIOD", color = Gold, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    GoldTextField(
                        value = tempStart,
                        onValueChange = { tempStart = it },
                        label = "Start Date (yyyy-MM-dd)"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GoldTextField(
                        value = tempEnd,
                        onValueChange = { tempEnd = it },
                        label = "End Date (yyyy-MM-dd)"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("QUICK SELECT", color = TextGray, fontSize = 11.sp, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("This Month", "Last Month", "This Year").forEach { label ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkGray)
                                    .clickable {
                                        val c = Calendar.getInstance()
                                        when (label) {
                                            "This Month" -> {
                                                val ym = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(c.time)
                                                tempStart = "$ym-01"
                                                tempEnd = "$ym-31"
                                            }
                                            "Last Month" -> {
                                                c.add(Calendar.MONTH, -1)
                                                val ym = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(c.time)
                                                tempStart = "$ym-01"
                                                tempEnd = "$ym-31"
                                            }
                                            "This Year" -> {
                                                val y = SimpleDateFormat("yyyy", Locale.getDefault()).format(c.time)
                                                tempStart = "$y-01-01"
                                                tempEnd = "$y-12-31"
                                            }
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(label, color = Gold, fontSize = 11.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    GoldButton(
                        text = "APPLY",
                        onClick = {
                            startDate = tempStart
                            endDate = tempEnd
                            showDatePicker = false
                        }
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = Black.copy(alpha = 0.08f),
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Routes.BUDGET) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("MY BUDGET", color = Gold, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Row {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Select Period", tint = Gold)
                    }
                    IconButton(onClick = { navController.navigate(Routes.SET_BUDGET_GOAL) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Budget", tint = Gold)
                    }
                }
            }

            // Date range display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BlackCard)
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$startDate  →  $endDate", color = TextGray, fontSize = 12.sp)
                    Icon(Icons.Filled.Edit, contentDescription = null, tint = Gold, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Budget Overview Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF1A1A00), Color(0xFF2A2000))))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BudgetStatItem("Total Budget", "R ${String.format("%.2f", goal)}", Gold)
                        BudgetStatItem("Spent", "R ${String.format("%.2f", spent)}", ErrorRed)
                        BudgetStatItem("Remaining", "R ${String.format("%.2f", remaining)}", SuccessGreen)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { progress.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = when {
                            progress > 0.9 -> ErrorRed
                            progress > 0.7 -> WarningOrange
                            else -> Gold
                        },
                        trackColor = DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${(progress * 100).toInt()}% of budget used", color = TextGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BudgetActionButton("Set Budget Goal", Icons.Filled.Flag, Modifier.weight(1f)) {
                    navController.navigate(Routes.SET_BUDGET_GOAL)
                }
                BudgetActionButton("Category Limits", Icons.Filled.Tune, Modifier.weight(1f)) {
                    navController.navigate(Routes.SET_CATEGORY_LIMITS)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("CATEGORY TOTALS FOR PERIOD", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            if (categories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlackCard)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Category, contentDescription = null, tint = TextGray, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No categories yet", color = TextGray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Create categories →",
                            color = Gold,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { navController.navigate(Routes.CREATE_CATEGORY) }
                        )
                    }
                }
            } else {
                categories.forEach { cat ->
                    val catTotal by viewModel.getTotalByCategory(cat.id, startDate, endDate)
                        .collectAsState(initial = 0.0)
                    val catSpent = catTotal ?: 0.0
                    val catProgress = if (cat.monthlyLimit > 0) (catSpent / cat.monthlyLimit).coerceIn(0.0, 1.0) else 0.0
                    val catColor = try {
                        Color(android.graphics.Color.parseColor(cat.colorHex))
                    } catch (e: Exception) { Gold }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BlackCard)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(catColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(cat.name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "R ${String.format("%.2f", catSpent)}",
                                        color = if (catProgress > 0.9) ErrorRed else TextWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (cat.monthlyLimit > 0) {
                                        Text(
                                            "/ R ${String.format("%.2f", cat.monthlyLimit)}",
                                            color = TextGray,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                            if (cat.monthlyLimit > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { catProgress.toFloat() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = when {
                                        catProgress > 0.9 -> ErrorRed
                                        catProgress > 0.7 -> WarningOrange
                                        else -> catColor
                                    },
                                    trackColor = DarkGray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Helper Composables ────────────────────────────────────────────────────────

@Composable
fun BudgetStatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = TextGray, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BudgetActionButton(
    label: String,
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BlackCard)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label, tint = Gold, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}