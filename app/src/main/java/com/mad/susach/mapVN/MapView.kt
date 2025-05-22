package com.mad.susach.mapVN

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack as ArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.susach.R
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MapView(navController: NavController, viewModel: MapViewModel = viewModel()) {
    val context = LocalContext.current
    val orange = Color(0xFFFF6600)
    val currentIndex = viewModel.currentIndex
    val maps = viewModel.maps

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Việt Nam (1009- nay)", color = orange, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = orange)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = orange)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = Color(0xFFFFF3E6)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, shape = MaterialTheme.shapes.large),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    // Hiệu ứng chuyển đổi bản đồ
                    AnimatedContent(
                        targetState = currentIndex,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { width -> width } with slideOutHorizontally { width -> -width }
                            } else {
                                slideInHorizontally { width -> -width } with slideOutHorizontally { width -> width }
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    ) { index ->
                        val resId = maps[index]
                        val entryName = context.resources.getResourceEntryName(resId)
                        val year = entryName.trimStart('_')
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = "Bản đồ năm $year",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(320.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Bản đồ Việt Nam - $year",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = orange,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.prevMap() },
                        enabled = currentIndex > 0,
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (currentIndex > 0) orange else Color(0x33FF6600),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = "Năm trước",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = { viewModel.nextMap() },
                        enabled = currentIndex < maps.size - 1,
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (currentIndex < maps.size - 1) orange else Color(0x33FF6600),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Năm sau",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val orange = Color(0xFFFF6600)
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = orange,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Trang chủ",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            label = { Text("Trang chủ", color = orange) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent // Không cần nền cam bo tròn phía dưới
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Handle Comments */ },
            icon = { Icon(Icons.Default.Comment, contentDescription = "Bình luận", tint = orange) },
            label = { Text("Bình luận", color = orange) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Handle Customize */ },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Tùy chỉnh", tint = orange) },
            label = { Text("Tùy chỉnh", color = orange) }
        )
    }
}