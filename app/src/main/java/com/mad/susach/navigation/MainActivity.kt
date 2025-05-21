package com.mad.susach.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mad.susach.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFfffbff) // Changed background color
            ) {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    username: String,
    notificationCount: Int,
    onNotificationClick: () -> Unit,
    onSearchClick: (String) -> Unit,
    navController: NavController
) {
    val saved = remember {
        listOf(
            SavedItem(
                title = "Nhà Triệu",
                imageRes = R.drawable.nhatrieu,
                year = "179 TCN - 111 TCN"
            ),
            SavedItem(
                title = "Bắc thuộc lần thứ nhất",
                imageRes = R.drawable.bacthuoclan1,
                year = "111 TCN - 40"
            )
        )
    }
    var selectedNav by remember { mutableIntStateOf(0) }
    var searchText by remember { mutableStateOf("Tìm gì đó...") }
    val context = LocalContext.current

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6F0))
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth(0.84f)
                .align(Alignment.TopCenter)
                .padding(horizontal = 0.dp)
                .padding(bottom = 56.dp) // Padding for BottomNavBar
        ) {
            item { // Header, Search, Saved Carousel
                Spacer(Modifier.height(30.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hôm nay bạn muốn\ntìm hiểu gì, $username?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.sitka_small_semibold)),
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
                    onClick = { onSearchClick(searchText) }
                )
                Spacer(Modifier.height(18.dp))
                Text("Đã lưu", fontWeight = FontWeight.SemiBold, fontSize = 21.sp)
                Spacer(Modifier.height(12.dp))
                SavedCarousel(items = saved, onClick = onNotificationClick)
                Spacer(Modifier.height(12.dp))
            }

            stickyHeader { // Sticky "Khám phá" title
                Text(
                    "Khám phá",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 21.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDF6F0)) // Background to prevent overlap
                        .padding(vertical = 12.dp) // Padding for spacing
                )
            }

            item { // ExploreSection and bottom spacer
                ExploreSection(
                    onTimelineClick = { navController.navigate(Screen.EraSelection.route) },
                    onMapClick = { /* TODO: Implement map click */ },
                    onRandomClick = { navController.navigate(Screen.RandomArticle.route) }
                )
                Spacer(Modifier.height(48.dp)) // 48.dp spacer at the bottom
            }
        }
        MainBottomNavBar(
            selected = selectedNav,
            onSelected = {
                selectedNav = it
                onNotificationClick()
            },
            context = context,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
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

data class SavedItem(val title: String, val imageRes: Int?, val year: String)

@Composable
fun SavedCard(item: SavedItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 32.dp) // leave space for year box
            ) {
                if (item.imageRes != null) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .height(78.dp)
                            .fillMaxWidth()
                    )
                } else {
                    Spacer(Modifier.height(8.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f, fill = false)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Color(0xFFFF6600),
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.year,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ExploreSection(
    onTimelineClick: () -> Unit,
    onMapClick: () -> Unit,
    onRandomClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clickable { onTimelineClick() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6600)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ex1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight()
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Theo\ndòng\nsử Việt",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 32.sp
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clickable { onMapClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6600)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lãnh Thổ\nqua các\nthời kỳ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ex2),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clickable { onRandomClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6600)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ex3),
                        contentDescription = null,
                        modifier = Modifier
                            .size(84.dp)
                            .padding(horizontal = 12.dp)
                    )
                    Text(
                        text = "Bài\nviết\nngẫu nhiên",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Trang chủ - Kiến thức")
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        username = "XXX",
        notificationCount = 5,
        onNotificationClick = {},
        onSearchClick = {},
        navController = rememberNavController()
    )
}

