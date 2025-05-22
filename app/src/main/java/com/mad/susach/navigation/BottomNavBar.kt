package com.mad.susach.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

// Data class to hold bottom navigation item properties
data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@Composable
fun MainBottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        BottomNavItem(
            label = "Kiến thức",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.AutoMirrored.Filled.MenuBook,
            route = Screen.Home.route
        ),
        BottomNavItem(
            label = "Luyện tập",
            selectedIcon = Icons.AutoMirrored.Filled.Rule, // Using auto-mirrored
            unselectedIcon = Icons.AutoMirrored.Filled.Rule,
            route = Screen.Practice.route
        ),
        BottomNavItem(
            label = "Người dùng",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Filled.AccountCircle,
            route = Screen.Profile.route
        )
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFFFFF3E0),
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSelected) Color(0xFFFF6600) else Color(0xFF3A3939),
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) Color(0xFFFF6600) else Color(0xFF3A3939),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFF6600),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFFFF6600),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // Or a subtle selection color like Color(0xFFFDF6F0).copy(alpha = 0.1f)
                )
            )
        }
    }
}
