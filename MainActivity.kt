package com.cashanova.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.CashanovaBackground
import com.cashanova.app.ui.screens.LoginScreen
import com.cashanova.app.ui.screens.RegisterScreen
import com.cashanova.app.ui.screens.DashboardScreen
import com.cashanova.app.ui.screens.BudgetScreen
import com.cashanova.app.ui.screens.SetBudgetGoalScreen
import com.cashanova.app.ui.screens.CreateCategoryScreen
import com.cashanova.app.ui.screens.SetCategoryLimitsScreen
import com.cashanova.app.ui.screens.ExpensesScreen
import com.cashanova.app.ui.screens.CreateExpenseScreen
import com.cashanova.app.ui.screens.SearchScreen
import com.cashanova.app.ui.screens.ExportScreen
import com.cashanova.app.ui.screens.RecurringExpenseScreen
import com.cashanova.app.ui.screens.InboxScreen
import com.cashanova.app.ui.theme.Black
import com.cashanova.app.ui.theme.CashanovaTheme
import com.cashanova.app.viewmodel.CashanovaViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashanovaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Black
                ) {
                    val navController = rememberNavController()
                    val viewModel: CashanovaViewModel = viewModel()

                    // Determine start destination
                    val startDestination = if (viewModel.sessionManager.isLoggedIn())
                        Routes.DASHBOARD else Routes.LOGIN

                    CashanovaBackground {
                        NavHost(
                        navController    = navController,
                        startDestination = startDestination
                    ) {
                        composable(Routes.LOGIN) {
                            LoginScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.REGISTER) {
                            RegisterScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.DASHBOARD) {
                            DashboardScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.BUDGET) {
                            BudgetScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.SET_BUDGET_GOAL) {
                            SetBudgetGoalScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.CREATE_CATEGORY) {
                            CreateCategoryScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.SET_CATEGORY_LIMITS) {
                            SetCategoryLimitsScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.EXPENSES) {
                            ExpensesScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.CREATE_EXPENSE) {
                            CreateExpenseScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.SEARCH) {
                            SearchScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.EXPORT) {
                            ExportScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.RECURRING_EXPENSE) {
                            RecurringExpenseScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Routes.INBOX) {
                            InboxScreen(navController = navController, viewModel = viewModel)
                        }
                        }
                    }
                }
            }
        }
    }
}
