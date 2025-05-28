package com.mad.susach.navigation

import android.content.Intent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mad.susach.R
import com.mad.susach.profile.ui.ProfileScreen
import com.mad.susach.profile.ui.ProfileViewModel
import com.mad.susach.saved.ui.SavedPostsActivity
import com.mad.susach.auth.login.ui.LoginActivity
import com.mad.susach.mapVN.MapView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

data class SavedItem(val title: String, val imageRes: Int?, val year: String)

// --- Trang chủ ---
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
            SavedItem("Nhà Triệu", R.drawable.nhatrieu, "179 TCN - 111 TCN"),
            SavedItem("Bắc thuộc lần thứ nhất", R.drawable.bacthuoclan1, "111 TCN - 40")
        )
    }
    
    val searchText by remember { mutableStateOf("Tìm gì đó...") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(Modifier.height(30.dp))
                // Lời chào
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Hôm nay bạn muốn\ntìm hiểu gì, $username?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.sitka_small_semibold)),
                        modifier = Modifier.weight(1f)
                    )

                    // Thông báo
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
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
                
                // Thanh tìm kiếm
                SearchBar(
                    placeholder = searchText,
                    onClick = { onSearchClick(searchText) }
                )
                Spacer(Modifier.height(18.dp))

                // "Đã lưu"
                Text(
                    "Đã lưu", 
                    fontWeight = FontWeight.SemiBold, 
                    fontSize = 21.sp
                )
                Spacer(Modifier.height(12.dp))
                SavedCarousel(items = saved, onClick = onNotificationClick)
                Spacer(Modifier.height(12.dp))
            }

            stickyHeader {
                // "Khám phá"
                Text(
                    "Khám phá",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 21.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDF6F0))
                        .padding(vertical = 12.dp)
                )
            }

            item {
                ExploreSection(
                    onTimelineClick = { navController.navigate(Screen.EraSelection.route) },
                    onMapClick = { navController.navigate(Screen.MapView.route) },
                    onRandomClick = { navController.navigate(Screen.RandomArticle.route) }
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// --- Thành phần UI ---
@Composable
// Thanh tìm kiếm chi tiết
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
        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFFF6600))
            Spacer(Modifier.width(8.dp))
            Text(placeholder, color = Color.Gray, fontSize = 16.sp)
        }
    }
}

// Carousel hiển thị các bài viết đã lưu
@Composable
fun SavedCarousel(items: List<SavedItem>, onClick: () -> Unit) {
    LazyRow {
        items(items) { item ->
            SavedCard(item = item, onClick = onClick)
            Spacer(Modifier.width(12.dp))
        }
    }
}

// Card hiển thị thông tin bài viết đã lưu
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
                    .padding(bottom = 32.dp)
            ) {
                // Hình ảnh bài viết
                item.imageRes?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier
                            .height(78.dp)
                            .fillMaxWidth()
                    )
                }
                Spacer(Modifier.height(8.dp))

                // Tiêu đề bài viết
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
            // Năm bài viết
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
        // "Theo dòng sử việt"
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
            // "Lãnh thổ qua các thời kỳ"
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
            
            // "Bài viết ngẫu nhiên"
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


