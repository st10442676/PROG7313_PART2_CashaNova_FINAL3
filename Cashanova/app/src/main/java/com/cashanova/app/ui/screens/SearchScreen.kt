package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.BottomNavBar
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel

//Phillips, B., Stewart, C. and Marsicano, K. 2019. Android Programming: The Big Nerd Ranch Guide. 4th ed. Atlanta: Big Nerd Ranch.

@Composable
fun SearchScreen(navController: NavController, viewModel: CashanovaViewModel) {
    var query by remember { mutableStateOf("") }
    val results by viewModel.searchExpenses(query).collectAsState(initial = emptyList())
    val categories by viewModel.getCategories().collectAsState(initial = emptyList())

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

    Scaffold(
        containerColor = Black.copy(alpha = 0.08f),
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Routes.SEARCH) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("SEARCH EXPENSES", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search by description...", color = TextGray) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Gold) },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = TextGray)
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Gold,
                    unfocusedBorderColor = MediumGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    cursorColor = Gold,
                    focusedContainerColor = BlackCard,
                    unfocusedContainerColor = BlackCard
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (query.isNotBlank()) {
                Text(
                    "${results.size} result${if (results.size != 1) "s" else ""} found",
                    color = TextGray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (results.isEmpty() && query.isNotBlank()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.SearchOff, contentDescription = null, tint = TextGray, modifier = Modifier.size(50.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No results for \"$query\"", color = TextGray, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(results) { expense ->
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(catColor.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(expense.description, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                        Text(
                                            "${cat?.name ?: "Unknown"} • ${expense.date}",
                                            color = TextGray, fontSize = 11.sp
                                        )
                                    }
                                }
                                Text(
                                    "R ${String.format("%.2f", expense.amount)}",
                                    color = ErrorRed,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}