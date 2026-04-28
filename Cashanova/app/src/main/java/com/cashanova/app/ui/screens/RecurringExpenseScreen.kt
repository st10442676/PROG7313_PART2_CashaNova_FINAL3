package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel

@Composable
fun RecurringExpenseScreen(navController: NavController, viewModel: CashanovaViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().background(Black).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.Repeat, contentDescription = null, tint = Gold, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("RECURRING EXPENSES", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Recurring expense management coming soon!", color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
        }
    }
}