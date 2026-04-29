package com.cashanova.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.theme.*

//Jemerov, D. and Isakova, S. 2017. Kotlin in Action. Shelter Island: Manning Publications.

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String,
    showBack: Boolean = false
) {
    val items = if (showBack) {
        listOf(
            NavItem("Budget",    Icons.Filled.BarChart,       Routes.BUDGET),
            NavItem("Expenses",  Icons.Filled.Receipt,        Routes.EXPENSES),
            NavItem("Dashboard", Icons.Filled.Speed,          Routes.DASHBOARD),
            NavItem("Search",    Icons.Filled.Search,         Routes.SEARCH),
            NavItem("Back",      Icons.Filled.ArrowBack,      "back")
        )
    } else {
        listOf(
            NavItem("Budget",    Icons.Filled.BarChart,       Routes.BUDGET),
            NavItem("Expenses",  Icons.Filled.Receipt,        Routes.EXPENSES),
            NavItem("Dashboard", Icons.Filled.Speed,          Routes.DASHBOARD),
            NavItem("Search",    Icons.Filled.Search,         Routes.SEARCH),
            NavItem("More",      Icons.Filled.Menu,           Routes.INBOX)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlackCard)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        if (item.route == "back") {
                            navController.popBackStack()
                        } else if (item.route != currentRoute) {
                            navController.navigate(item.route) {
                                popUpTo(Routes.DASHBOARD) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) Gold else TextGray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.label,
                    fontSize = 10.sp,
                    color = if (isSelected) Gold else TextGray
                )
            }
        }
    }
}