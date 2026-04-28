package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
fun ExpensesScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val cal = Calendar.getInstance()
    val defaultStart = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(cal.time) + "-01"
    val defaultEnd = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(cal.time) + "-31"
    val monthLabel = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)

    var startDate by remember { mutableStateOf(defaultStart) }
    var endDate by remember { mutableStateOf(defaultEnd) }
    var showDatePicker by remember { mutableStateOf(false) }
    var tempStart by remember { mutableStateOf(defaultStart) }
    var tempEnd by remember { mutableStateOf(defaultEnd) }
    var selectedPhotoPath by remember { mutableStateOf<String?>(null) }

    val expenses by viewModel.getExpensesByPeriod(startDate, endDate).collectAsState(initial = emptyList())
    val categories by viewModel.getCategories().collectAsState(initial = emptyList())
    val totalSpent by viewModel.getTotalSpent(startDate, endDate).collectAsState(initial = 0.0)

    val iconMap = mapOf<String, ImageVector>(
        "ShoppingCart" to Icons.Filled.ShoppingCart,
        "Restaurant" to Icons.Filled.Restaurant,
        "DirectionsCar" to Icons.Filled.DirectionsCar,
        "Home" to Icons.Filled.Home,
        "LocalHospital" to Icons.Filled.LocalHospital,
        "School" to Icons.Filled.School,
        "Movie" to Icons.Filled.Movie,
        "Checkroom" to Icons.Filled.Checkroom,
        "Pets" to Icons.Filled.Pets,
        "SportsSoccer" to Icons.Filled.SportsSoccer,
        "Flight" to Icons.Filled.Flight,
        "Phone" to Icons.Filled.Phone,
        "Wifi" to Icons.Filled.Wifi,
        "LocalGasStation" to Icons.Filled.LocalGasStation,
        "Coffee" to Icons.Filled.Coffee,
        "Savings" to Icons.Filled.Savings
    )

    // ── Photo viewer dialog ───────────────────────────────────────────────────
    if (selectedPhotoPath != null) {
        Dialog(onDismissRequest = { selectedPhotoPath = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BlackCard)
                    .padding(8.dp)
            ) {
                Column {
                    AsyncImage(
                        model = selectedPhotoPath,
                        contentDescription = "Receipt",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GoldButton(text = "Close", onClick = { selectedPhotoPath = null })
                }
            }
        }
    }

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
                    Text("SELECT DATE RANGE", color = Gold, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
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

                    // Quick select buttons
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
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Routes.EXPENSES) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.CREATE_EXPENSE) },
                containerColor = Gold,
                contentColor = Black
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        }
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
                Text("EXPENSES", color = Gold, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Select Period", tint = Gold)
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

            Spacer(modifier = Modifier.height(12.dp))

            // Total spent card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BlackCard)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total for Period", color = TextGray, fontSize = 12.sp)
                        Text(
                            "R ${String.format("%.2f", totalSpent ?: 0.0)}",
                            color = Gold,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text("${expenses.size} transactions", color = TextGray, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Receipt, contentDescription = null, tint = TextGray, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No expenses for this period", color = TextGray, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tap + to add an expense",
                            color = Gold,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { navController.navigate(Routes.CREATE_EXPENSE) }
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(expenses) { expense ->
                        val cat = categories.find { it.id == expense.categoryId }
                        val catColor = try {
                            Color(android.graphics.Color.parseColor(cat?.colorHex ?: "#FFD700"))
                        } catch (e: Exception) { Gold }
                        val catIcon = iconMap[cat?.iconName] ?: Icons.Filled.Receipt

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(BlackCard)
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .background(catColor.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(22.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(expense.description, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                            Text("${cat?.name ?: "Unknown"} • ${expense.date}", color = TextGray, fontSize = 11.sp)
                                            if (expense.startTime.isNotBlank()) {
                                                Text("${expense.startTime} – ${expense.endTime}", color = TextGray, fontSize = 11.sp)
                                            }
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("- R ${String.format("%.2f", expense.amount)}", color = ErrorRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        Row {
                                            // View photo button
                                            if (expense.photoPath != null) {
                                                IconButton(
                                                    onClick = { selectedPhotoPath = expense.photoPath },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(Icons.Filled.Photo, contentDescription = "View Receipt", tint = Gold, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteExpense(expense) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = TextGray, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}