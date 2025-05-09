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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.susach.R
import com.mad.susach.navbar.MainBottomNavBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                username = "XXX",
                onNotificationClick = {},
                onSearchClick = {},
                onTimelineClick = {},
                onTerritoryClick = {},
                onCharacterClick = {},
                onContinueReadingClick = {},
                onNavSelected = {}
            )
        }
    }
}

@Composable
fun HomeScreen(
    username: String,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    onTimelineClick: () -> Unit,
    onTerritoryClick: () -> Unit,
    onCharacterClick: () -> Unit,
    onContinueReadingClick: () -> Unit,
    onNavSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val continueReading = remember {
        listOf(
            ContinueReadingItem(
                title = "Chiến Dịch Hồ Chí Minh (1975)",
                imageRes = R.drawable.ic_launcher_foreground,
                progress = 0.7f
            ),
            ContinueReadingItem(
                title = "Chiến tranh biên giới Việt - Trung (1979)",
                imageRes = R.drawable.ic_launcher_foreground,
                progress = 0.3f
            )
        )
    }
    var selectedNav by remember { mutableStateOf(0) }
    val context = LocalContext.current
    Column(
        modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6F0))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hôm nay bạn muốn\ntìm hiểu gì, $username?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Thông báo",
                    tint = Color(0xFFFF6600)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        SearchBar(onClick = onSearchClick)
        Spacer(Modifier.height(16.dp))
        Text("Đọc tiếp", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        ContinueReadingCarousel(items = continueReading, onClick = onContinueReadingClick)
        Spacer(Modifier.height(24.dp))
        Text("Khám phá", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        ExploreSection(
            onTimelineClick = onTimelineClick,
            onTerritoryClick = onTerritoryClick,
            onCharacterClick = onCharacterClick
        )
        Spacer(Modifier.weight(1f))
        MainBottomNavBar(selected = selectedNav, onSelected = {
            selectedNav = it
            onNavSelected(it)
        }, context = context)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun SearchBar(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFFF6600))
            Spacer(Modifier.width(8.dp))
            Text("Hai Bà Trưng", color = Color.Gray, fontSize = 16.sp)
        }
    }
}

@Composable
fun ContinueReadingCarousel(items: List<ContinueReadingItem>, onClick: () -> Unit) {
    LazyRow {
        items(items) { item ->
            ContinueReadingCard(item = item, onClick = onClick)
            Spacer(Modifier.width(12.dp))
        }
    }
}

@Composable
fun ContinueReadingCard(item: ContinueReadingItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
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
                progress = item.progress,
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

data class ContinueReadingItem(val title: String, val imageRes: Int, val progress: Float)

@Composable
fun ExploreSection(
    onTimelineClick: () -> Unit,
    onTerritoryClick: () -> Unit,
    onCharacterClick: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFF6600))
                    .clickable { onTimelineClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("Theo\ndòng sử Việt", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .weight(1f)
                    .height(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFF6600).copy(alpha = 0.9f))
                    .clickable { onTerritoryClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("Lãnh thổ\nqua các thời kỳ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.width(12.dp))
            Box(
                Modifier
                    .weight(1f)
                    .height(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFF6600).copy(alpha = 0.9f))
                    .clickable { onCharacterClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("Nhân vật\nlịch sử", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "Trang chủ - Kiến thức")
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        username = "XXX",
        onNotificationClick = {},
        onSearchClick = {},
        onTimelineClick = {},
        onTerritoryClick = {},
        onCharacterClick = {},
        onContinueReadingClick = {},
        onNavSelected = {}
    )
}
