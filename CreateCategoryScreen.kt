package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.components.GoldTextField
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel

@Composable
fun CreateCategoryScreen(navController: NavController, viewModel: CashanovaViewModel) {
    var categoryName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("ShoppingCart") }
    var selectedColor by remember { mutableStateOf("#FFD700") }
    var saved by remember { mutableStateOf(false) }

    val categories by viewModel.getCategories().collectAsState(initial = emptyList())

    val iconOptions = listOf(
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

    val colorOptions = listOf(
        "#FFD700", "#FF4444", "#4CAF50", "#2196F3",
        "#FF9800", "#9C27B0", "#00BCD4", "#F06292",
        "#8BC34A", "#FF5722", "#607D8B", "#795548"
    )

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
            Text("CREATE CATEGORY", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        GoldTextField(value = categoryName, onValueChange = { categoryName = it; saved = false }, label = "Category Name")

        Spacer(modifier = Modifier.height(24.dp))
        Text("CHOOSE ICON", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(iconOptions) { (name, icon) ->
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedIcon == name) Gold.copy(alpha = 0.3f) else BlackCard)
                        .border(
                            width = if (selectedIcon == name) 2.dp else 0.dp,
                            color = if (selectedIcon == name) Gold else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedIcon = name; saved = false },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = name, tint = if (selectedIcon == name) Gold else TextGray, modifier = Modifier.size(24.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("CHOOSE COLOUR", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(colorOptions) { hex ->
                val c = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Gold }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(c)
                        .border(
                            width = if (selectedColor == hex) 3.dp else 0.dp,
                            color = TextWhite,
                            shape = CircleShape
                        )
                        .clickable { selectedColor = hex; saved = false }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Preview
        if (categoryName.isNotBlank()) {
            val previewColor = try { Color(android.graphics.Color.parseColor(selectedColor)) } catch (e: Exception) { Gold }
            val previewIcon = iconOptions.find { it.first == selectedIcon }?.second ?: Icons.Filled.Star
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BlackCard)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(previewColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(previewIcon, contentDescription = null, tint = previewColor, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(categoryName, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (saved) {
            Text("✓ Category created!", color = SuccessGreen, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        GoldButton(
            text = "CREATE CATEGORY",
            onClick = {
                if (categoryName.isNotBlank()) {
                    viewModel.createCategory(categoryName, selectedIcon, selectedColor)
                    categoryName = ""
                    saved = true
                }
            },
            enabled = categoryName.isNotBlank()
        )

        // Existing categories
        if (categories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("YOUR CATEGORIES", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
            categories.forEach { cat ->
                val catColor = try { Color(android.graphics.Color.parseColor(cat.colorHex)) } catch (e: Exception) { Gold }
                val catIcon = iconOptions.find { it.first == cat.iconName }?.second ?: Icons.Filled.Star
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BlackCard)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(catColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(cat.name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    IconButton(onClick = { viewModel.deleteCategory(cat) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

//Jemerov, D. and Isakova, S. 2017. Kotlin in Action. Shelter Island: Manning Publications.