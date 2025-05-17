package com.mad.susach.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.susach.R
import com.mad.susach.navigation.AppNavigation
import com.mad.susach.navigation.MainBottomNavBar
import com.mad.susach.navigation.Screen
import com.mad.susach.timeline.ui.eralselection.EraSelectionScreen
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.shadow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun HomeScreen(
    username: String,
    notificationCount: Int,
    onNotificationClick: () -> Unit,
    onSearchClick: (String) -> Unit, // Pass search phrase
    onTimelineClick: () -> Unit,
    onTerritoryClick: () -> Unit,
    onRandomArticleClick: () -> Unit,
    onSavedClick: () -> Unit,
    onNavSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: androidx.navigation.NavController,
    searchPlaceholder: String = "Tìm gì đó..."
) {
    val Saved = remember {
        listOf(
            SavedItem(
                title = "Chiến Dịch Hồ Chí Minh (1975)",
                imageRes = R.mipmap.ic_launcher_foreground,
                progress = 0.7f
            ),
            SavedItem(
                title = "Chiến tranh biên giới Việt - Trung (1979)",
                imageRes = R.mipmap.ic_launcher_foreground,
                progress = 0.3f
            )
        )
    }
    var selectedNav by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf(searchPlaceholder) }
    val context = LocalContext.current

    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6F0))
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.84f)
                .align(Alignment.TopCenter)
                .padding(horizontal = 0.dp)
        ) {
            Spacer(Modifier.height(30.dp))

            Row(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Hôm nay bạn muốn\ntìm hiểu gì, $username?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(com.mad.susach.R.font.sitka_small_semibold)),
                    modifier = Modifier.weight(1f)
                )
                
                Box {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Thông báo",
                            tint = Color(0xFFFF6600),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    if (notificationCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-2).dp, y = 2.dp)
                                .size(18.dp)
                                .background(Color.Red, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                                color = Color.White,
                                fontSize = 10.sp * 1.5f, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            SearchBar(
                placeholder = searchText,
                onClick = {
                    onSearchClick(searchText)
                }
            )

            Spacer(Modifier.height(18.dp))

            Text("Đã lưu", fontWeight = FontWeight.SemiBold, fontSize = 21.sp)

            Spacer(Modifier.height(12.dp))

            SavedCarousel(items = Saved, onClick = onSavedClick)

            Spacer(Modifier.height(24.dp))

            Text("Khám phá", fontWeight = FontWeight.SemiBold, fontSize = 21.sp)

            Spacer(Modifier.height(12.dp))

            ExploreSection(
                onTimelineClick = { navController.navigate(Screen.EraSelection.route) },
                onTerritoryClick = onTerritoryClick,
                onRandomArticleClick = onRandomArticleClick 
            )

            Spacer(Modifier.weight(1f))
        }
        MainBottomNavBar(
            selected = selectedNav,
            onSelected = {
                selectedNav = it
                onNavSelected(it)
            },
            context = context,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun SearchBar(placeholder: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFFF6600))
                Spacer(Modifier.width(8.dp))
                Text(placeholder, color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SavedCarousel(items: List<SavedItem>, onClick: () -> Unit) {
    LazyRow {
        items(items) { item ->
            SavedCard(item = item, onClick = onClick)
            Spacer(Modifier.width(12.dp))
        }
    }
}

@Composable
fun SavedCard(item: SavedItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { item.progress }, // Use lambda overload to avoid deprecation
                color = Color(0xFFFF6600),
                trackColor = Color(0xFFF2F2F2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

data class SavedItem(val title: String, val imageRes: Int, val progress: Float)

@Composable
fun ExploreSection(
    onTimelineClick: () -> Unit,
    onTerritoryClick: () -> Unit,
    onRandomArticleClick: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        //"Theo dòng sử Việt"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onTimelineClick() },
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6600))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Theo\ndòng sử Việt",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    lineHeight = 30.sp
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        // Bottom row: two square buttons
        Row(Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clickable { onTerritoryClick() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6600))
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Lãnh Thổ\nqua các thời kỳ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 20.sp)
                    }
                }
            }
            Spacer(Modifier.width(24.dp))
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clickable { onRandomArticleClick() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6600))
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("?", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
                        Text("Bài viết\nngẫu nhiên", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Trang chủ - Kiến thức")
@Composable
fun HomeScreenPreview() {
    val mockNavController = rememberNavController()
    HomeScreen(
        username = "XXX",
        notificationCount = 5, // Provide a mock value
        onNotificationClick = {},
        onSearchClick = {},
        onTimelineClick = {},
        onTerritoryClick = {},
        onRandomArticleClick = {},
        onSavedClick = {},
        onNavSelected = {},
        navController = mockNavController,
        searchPlaceholder = "Chiến dịch Điện Biên Phủ"
    )
}

