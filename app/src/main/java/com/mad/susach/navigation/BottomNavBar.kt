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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainBottomNavBar(
        selected: Int,
        onSelected: (Int) -> Unit,
        modifier: Modifier = Modifier,
        context: Context? = null
) {
    val ctx = context ?: LocalContext.current

    val navItems =
            listOf(
                    Triple("Kiến thức", Icons.AutoMirrored.Filled.MenuBook, Icons.Filled.Book),
                    Triple("Luyện tập", Icons.AutoMirrored.Filled.Rule, Icons.AutoMirrored.Filled.Rule),
                    Triple("Người dùng", Icons.Filled.AccountCircle, Icons.Filled.Person)
            )

    NavigationBar(modifier = modifier, containerColor = Color(0xFFFFF3E0), tonalElevation = 8.dp) {
        navItems.forEachIndexed { index, triple ->
            val (label, selectedIcon, unselectedIcon) = triple
            val isSelected = selected == index

            NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        onSelected(index)
                        handleNavBarNavigation(ctx, index)
                    },
                    icon = {
                        Icon(
                                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                contentDescription = label,
                                tint = if (isSelected) Color(0xFFFF6600) else Color(0xFF3A3939),
                                modifier = Modifier.size(32.dp)
                        )
                    },
                    label = {
                        Text(
                                text = label,
                                color = if (isSelected) Color(0xFFFF6600) else Color(0xFF3A3939),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                        )
                    },
                    colors =
                            NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                    selectedIconColor = Color(0xFFFF6600),
                                    unselectedIconColor = Color(0xFF3A3939),
                                    selectedTextColor = Color(0xFFFF6600),
                                    unselectedTextColor = Color(0xFF3A3939)
                            )
            )
        }
    }
}

private fun handleNavBarNavigation(ctx: Context, index: Int) {
    when (index) {
        0 -> ctx.startActivity(
            Intent(ctx, MainActivity::class.java)
        )
        2 -> ctx.startActivity(
            Intent(ctx, com.mad.susach.profile.ui.ProfileActivity::class.java)
        )
    }
}

@Preview(showBackground = true, name = "MainBottomNavBar Preview")
@Composable
fun MainBottomNavBarPreview() {
    MainBottomNavBar(selected = 0, onSelected = {})
}
