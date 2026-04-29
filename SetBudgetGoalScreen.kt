package com.cashanova.app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.components.GoldTextField
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Phillips, B., Stewart, C. and Marsicano, K. 2019. Android Programming: The Big Nerd Ranch Guide. 4th ed. Atlanta: Big Nerd Ranch.

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetBudgetGoalScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val today = LocalDate.now()
    val monthYear = today.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    val monthLabel = today.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

    val existingBudget by viewModel.getBudgetForMonth(monthYear).collectAsState(initial = null)

    var totalGoal by remember { mutableStateOf("") }
    var minimumGoal by remember { mutableStateOf("") }
    var monthlyIncome by remember { mutableStateOf("") }
    var fixedExpenses by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }

    LaunchedEffect(existingBudget) {
        existingBudget?.let {
            if (totalGoal.isEmpty()) totalGoal = it.totalGoal.toString()
            if (minimumGoal.isEmpty()) minimumGoal = it.minimumGoal.toString()
            if (monthlyIncome.isEmpty()) monthlyIncome = it.monthlyIncome.toString()
            if (fixedExpenses.isEmpty()) fixedExpenses = it.fixedExpenses.toString()
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

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("SET BUDGET GOAL", color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        }
        Text(monthLabel, color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(start = 48.dp))

        Spacer(modifier = Modifier.height(32.dp))

        // Total Goal
        Text("SPENDING LIMIT", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        GoldTextField(
            value = totalGoal,
            onValueChange = { totalGoal = it },
            label = "Maximum Budget Goal (R)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoldTextField(
            value = minimumGoal,
            onValueChange = { minimumGoal = it },
            label = "Minimum Savings Goal (R)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("INCOME & FIXED EXPENSES", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))

        GoldTextField(
            value = monthlyIncome,
            onValueChange = { monthlyIncome = it },
            label = "Monthly Income (R)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoldTextField(
            value = fixedExpenses,
            onValueChange = { fixedExpenses = it },
            label = "Fixed Monthly Expenses (R)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        // Calculated Disposable Income
        val income = monthlyIncome.toDoubleOrNull() ?: 0.0
        val fixed = fixedExpenses.toDoubleOrNull() ?: 0.0
        val disposable = income - fixed
        if (income > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlackCard, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("DISPOSABLE INCOME", color = TextGray, fontSize = 11.sp, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "R ${String.format("%.2f", disposable)}",
                        color = if (disposable >= 0) SuccessGreen else ErrorRed,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        if (saved) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "✓ Budget saved successfully!",
                color = SuccessGreen,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        GoldButton(
            text = "SAVE BUDGET",
            onClick = {
                val goal = totalGoal.toDoubleOrNull() ?: 0.0
                viewModel.saveBudget(
                    monthYear = monthYear,
                    totalGoal = goal,
                    minimumGoal = minimumGoal.toDoubleOrNull() ?: 0.0,
                    monthlyIncome = income,
                    fixedExpenses = fixed
                )
                saved = true
            },
            enabled = totalGoal.isNotBlank()
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}