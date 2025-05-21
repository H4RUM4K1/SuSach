package com.mad.susach.article.ui

import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mad.susach.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleView(eventId: String?, navController: NavController, viewModel: ArticleViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(eventId) {
        if (eventId != null) {
            if (eventId == "random") {
                viewModel.fetchRandomEvent()
            } else {
                viewModel.fetchEvent(eventId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.event?.name ?: "Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { 
                        uiState.event?.id?.let { eventId ->
                            viewModel.toggleSavePost(eventId)
                        }
                    }) {
                        Icon(
                            imageVector = if (uiState.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = if (uiState.isSaved) "Unsave Post" else "Save Post"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.errorMessage ?: "Unknown error.")
                }
            }
            uiState.event != null -> {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Name
                    Text(
                        text = uiState.event.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Summary (bold)
                    Text(
                        text = uiState.event.summary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Image
                    val context = LocalContext.current
                    val imageResId = uiState.event.imageURL.let { imageName ->
                        context.resources.getIdentifier(imageName, "drawable", context.packageName)
                    }.takeIf { it != 0 } ?: R.drawable.haibatrung
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Event Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // imageContent (italic, small)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.event.imageContent,
                            fontSize = 13.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // content (normal)
                    Text(
                        text = uiState.event.contents,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate("home") {
                popUpTo("home") { inclusive = true }
                launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            label = { Text("Trang chủ") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Handle Comments */ },
            icon = { Icon(Icons.Default.Comment, contentDescription = "Bình luận") },
            label = { Text("Bình luận") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Handle Customize */ },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Tùy chỉnh") },
            label = { Text("Tùy chỉnh") }
        )

    }
}