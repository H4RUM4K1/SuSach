package com.susach

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.susach.model.Article

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                username = "XXX",
                readingArticles = sampleReadingArticles,
                onNotificationClick = { /* TODO: Hiện giao diện thông báo */ },
                onSearchClick = { /* TODO: Chuyển sang giao diện tìm kiếm */ },
                onTimelineClick = { /* TODO: Chuyển sang giao diện timeline */ },
                onLandClick = { /* TODO: Chuyển sang giao diện lãnh thổ */ },
                onCharacterClick = { /* TODO: Chuyển sang giao diện nhân vật */ },
                onArticleClick = { /* TODO: Xử lý khi nhấn vào bài đọc tiếp */ }
            )
        }
    }
}

@Composable
fun HomeScreen(
    username: String,
    readingArticles: List<Article>,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    onTimelineClick: () -> Unit,
    onLandClick: () -> Unit,
    onCharacterClick: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hôm nay bạn muốn tìm hiểu gì, $username?", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notifications),
                            contentDescription = "Thông báo",
                            tint = Color(0xFFFF6600)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Hai Bà Trưng") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Tìm kiếm",
                        tint = Color(0xFFFF6600)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSearchClick() },
                enabled = false, // Chỉ để click chuyển giao diện
                readOnly = true
            )
            // Carousel "Đọc tiếp"
            Text(
                text = "Đọc tiếp",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(readingArticles) { article ->
                    ReadingArticleCard(article = article, onClick = { onArticleClick(article) })
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
            // Khám phá
            Text(
                text = "Khám phá",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Button(
                    onClick = onTimelineClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6600)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text("Theo dòng sử Việt", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onLandClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6600)),
                        modifier = Modifier.weight(1f).height(80.dp)
                    ) {
                        Text("Lãnh thổ qua các thời kỳ", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onCharacterClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6600)),
                        modifier = Modifier.weight(1f).height(80.dp)
                    ) {
                        Text("Nhân vật lịch sử", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingArticleCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // TODO: Hiển thị hình ảnh nếu có
            Text(article.title, fontWeight = FontWeight.Bold, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            // Progress bar
            LinearProgressIndicator(progress = 0.5f, color = Color(0xFFFF6600), modifier = Modifier.fillMaxWidth())
            Text("50% đã đọc", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End))
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO: Chuyển tab Kiến thức */ },
            icon = { Icon(painterResource(id = R.drawable.ic_book), contentDescription = "Kiến thức", tint = Color(0xFFFF6600)) },
            label = { Text("Kiến thức") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO: Chuyển tab Luyện tập */ },
            icon = { Icon(painterResource(id = R.drawable.ic_practice), contentDescription = "Luyện tập", tint = Color(0xFF222222)) },
            label = { Text("Luyện tập") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO: Chuyển tab Người dùng */ },
            icon = { Icon(painterResource(id = R.drawable.ic_user), contentDescription = "Người dùng", tint = Color(0xFF222222)) },
            label = { Text("Người dùng") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        username = "Người dùng",
        readingArticles = sampleReadingArticles,
        onNotificationClick = {},
        onSearchClick = {},
        onTimelineClick = {},
        onLandClick = {},
        onCharacterClick = {},
        onArticleClick = {}
    )
}

// Sample data for preview/demo
val sampleReadingArticles = listOf(
    Article(id = "1", title = "Chiến Dịch Hồ Chí Minh (1975)", group = "Event", wikiSite = "vi.wikipedia.org"),
    Article(id = "2", title = "Chiến tranh biên giới Việt - Trung (1979)", group = "Event", wikiSite = "vi.wikipedia.org")
)
