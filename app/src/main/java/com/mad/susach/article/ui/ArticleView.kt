package com.mad.susach.article.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.R
import com.mad.susach.navigation.Screen
import java.util.regex.Pattern

private val NETWORK_URL_PATTERN = Pattern.compile(
    "^(http|https|ftp)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?\$|^www\\.[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?\$"
)

fun isNetworkUrl(url: String?): Boolean {
    if (url.isNullOrBlank()) {
        return false
    }
    val matches = NETWORK_URL_PATTERN.matcher(url).matches()
    return matches
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleView(eventId: String?, navController: NavController, viewModel: ArticleViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState().value
    val currentEvent = uiState.event // Store event in a local variable for safe usage

    // State cho tuỳ chỉnh
    var showSettings by remember { mutableStateOf(false) }
    var bgColor by remember { mutableStateOf(Color.White) }
    var fontSize by remember { mutableStateOf(16f) }

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
                title = { Text(currentEvent?.name ?: "Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { 
                        viewModel.toggleSavePost()
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
            eventId?.let { id ->
                BottomNavigationBar(
                    navController = navController,
                    eventId = id,
                    onCustomizeClick = { showSettings = true }
                )
            }
        }
    ) { paddingValues ->
        // Hiển thị dialog tuỳ chỉnh nếu cần
        ArticleSettingsDialog(
            show = showSettings,
            currentBgColor = bgColor,
            currentFontSize = fontSize,
            onBgColorChange = { bgColor = it },
            onFontSizeChange = { fontSize = it },
            onDismiss = { showSettings = false }
        )

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
                    Text(text = uiState.errorMessage ?: "Unknown error")
                }
            }
            currentEvent != null -> {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                        .padding(16.dp)
                        .background(bgColor)
                ) {
                    Text(
                        text = currentEvent.name,
//                        style = MaterialTheme.typography.headlineSmall.copy(
//                            fontWeight = FontWeight.Medium
//                        )
                        fontSize = (fontSize + 6).sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentEvent.summary,
//                        style = MaterialTheme.typography.titleMedium.copy(
//                           fontWeight = FontWeight.Bold
//                        )
                        fontSize = (fontSize).sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val imageUrl = currentEvent.imageURL

                    if (imageUrl.isNotBlank()) {
                        val context = LocalContext.current
                        val isNetwork = isNetworkUrl(imageUrl)

                        if (isNetwork) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                                contentDescription = "Event Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else { 
                            val imageResId = remember(imageUrl, context) {
                                val id = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
                                if (id != 0) {
                                    id 
                                } else {
                                    android.R.drawable.ic_menu_report_image
                                }
                            }
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = "Event Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = currentEvent.imageContent,
//                                style = MaterialTheme.typography.bodySmall.copy(
//                                    fontStyle = FontStyle.Italic
//                                ),
                                fontSize = 13.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = currentEvent.contents,
//                        style = MaterialTheme.typography.bodyMedium
                        fontSize = fontSize.sp
                    )
                }
            }
            else -> {
                 Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No event data available.")
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    eventId: String,
    onCustomizeClick: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Trang chủ") },
            label = { Text("Trang chủ") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { 
                navController.navigate(Screen.Comments.createRoute(eventId)) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = "Bình luận") },
            label = { Text("Bình luận") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onCustomizeClick() },
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Tùy chỉnh") }, // Corrected direct usage
            label = { Text("Tùy chỉnh") }
        )
    }
}