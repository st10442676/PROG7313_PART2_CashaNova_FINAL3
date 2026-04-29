package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.*
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel

//Griffiths, D. and Griffiths, D. 2021. Head First Android Development: A Learner's Guide to Building Android Apps with Kotlin. 3rd ed. Sebastopol: O'Reilly Media.

@Composable
fun SetCategoryLimitsScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val categories by viewModel.getCategories().collectAsState(initial = emptyList())
    var saved by remember { mutableStateOf(false) }

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

    val limitStates = remember(categories) {
        mutableStateMapOf<Int, String>().also { map ->
            categories.forEach { cat ->
                map[cat.id] = if (cat.monthlyLimit > 0) cat.monthlyLimit.toString() else ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("CATEGORY LIMITS", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Set a monthly spending limit for each category.", color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))

        Spacer(modifier = Modifier.height(24.dp))

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No categories yet.\nCreate some first!", color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        } else {
            categories.forEach { cat ->
                val catColor = try { Color(android.graphics.Color.parseColor(cat.colorHex)) } catch (e: Exception) { Gold }
                val catIcon = iconMap[cat.iconName] ?: Icons.Filled.Star

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlackCard)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(catColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cat.name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = limitStates[cat.id] ?: "",
                                onValueChange = { limitStates[cat.id] = it; saved = false },
                                placeholder = { Text("No limit", color = TextGray, fontSize = 12.sp) },
                                prefix = { Text("R ", color = Gold, fontSize = 14.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Gold,
                                    unfocusedBorderColor = MediumGray,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    cursorColor = Gold
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (saved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("✓ Limits saved!", color = SuccessGreen, fontSize = 14.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(16.dp))

            GoldButton(
                text = "SAVE LIMITS",
                onClick = {
                    categories.forEach { cat ->
                        val limit = limitStates[cat.id]?.toDoubleOrNull() ?: 0.0
                        viewModel.updateCategoryLimit(cat, limit)
                    }
                    saved = true
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}