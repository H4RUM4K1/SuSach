package com.mad.susach.navbar

import android.content.Context
import android.content.Intent
import com.mad.susach.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.Icon
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Book
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainBottomNavBar(
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    context: Context? = null
) {
    val ctx = context ?: LocalContext.current
    val items = listOf(
        // Kiến thức
        Triple("Kiến thức", Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Filled.MenuBook),
        // Luyện tập
        Triple("Luyện tập", Icons.AutoMirrored.Filled.Rule, Icons.AutoMirrored.Filled.Rule),
        // Người dùng
        Triple("Người dùng", Icons.Filled.AccountCircle, Icons.Filled.Person)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xFFFCF8F3))
            .height(96.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, triple ->
                val (label, selectedIcon, unselectedIcon) = triple
                val isSelected = selected == index
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(32.dp))
                        .padding(vertical = 4.dp)
                        .height(80.dp)
                        .padding(horizontal = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(onClick = {
                                onSelected(index)
                                when (index) {
                                    0 -> ctx.startActivity(Intent(ctx, com.mad.susach.main.MainActivity::class.java))
                                    2 -> ctx.startActivity(Intent(ctx, com.mad.susach.profile.ui.ProfileActivity::class.java))
                                }
                            })
                    ) {
                        Icon(
                            imageVector = if (isSelected) selectedIcon else unselectedIcon,
                            contentDescription = label,
                            tint = if (isSelected) Color(0xFFFF6600) else Color(0xFF3A3939),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = label,
                            color = if (isSelected) Color(0xFFFF6600) else Color(0xFF3A3939),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 52.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainBottomNavBarPreview() {
    MainBottomNavBar(selected = 0, onSelected = {})
}
