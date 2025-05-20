package com.mad.susach.article.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Corrected import
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.Comment // Corrected import
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mad.susach.R
import coil.compose.rememberAsyncImagePainter // Added Coil import

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") // Corrected usage
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
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
                    val imageUrl = uiState.event.imageURL
                    val context = LocalContext.current

                    if (imageUrl.isNotBlank()) {
                        val isNetworkUrl = imageUrl.startsWith("http://") || imageUrl.startsWith("https://")
                        if (isNetworkUrl) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = imageUrl,
                                    placeholder = painterResource(id = R.drawable.haibatrung),
                                    error = painterResource(id = R.drawable.haibatrung)
                                ),
                                contentDescription = "Event Image from URL",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Assume it's a local drawable name
                            val imageResId = context.resources.getIdentifier(
                                imageUrl, "drawable", context.packageName
                            ).takeIf { it != 0 } ?: R.drawable.haibatrung // Fallback to default
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = "Event Image from Drawable",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        // Fallback for blank or null imageUrl
                        Image(
                            painter = painterResource(id = R.drawable.haibatrung),
                            contentDescription = "Default Event Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

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
                    val contentToDisplay = uiState.fetchedWikipediaContent ?: uiState.event.contents
                    // Log the content being considered
                    // uiState.event is non-null here based on screen logic, direct access is safe
                    android.util.Log.d("ArticleView", "Event ID: ${uiState.event.id}")
                    android.util.Log.d("ArticleView", "Fetched Wikipedia Content (first 200 chars): ${uiState.fetchedWikipediaContent?.take(200)}...")
                    android.util.Log.d("ArticleView", "Event Contents (first 200 chars): ${uiState.event.contents.take(200)}...")
                    android.util.Log.d("ArticleView", "Content to display (first 200 chars): ${contentToDisplay.take(200)}...")

                    if (contentToDisplay.isNotBlank()) {
                        // Updated and more comprehensive HTML check
                        // isNotBlank() ensures contentToDisplay is not null, so direct .contains is safe
                        val isHtmlContent = contentToDisplay.length > 20 && // Heuristic: very short strings are less likely to be complex HTML
                                           (contentToDisplay.contains("<html", ignoreCase = true) ||
                                           contentToDisplay.contains("<body", ignoreCase = true) ||
                                           contentToDisplay.contains("<div", ignoreCase = true) ||
                                           contentToDisplay.contains("<p", ignoreCase = true) ||
                                           contentToDisplay.contains("<span", ignoreCase = true) ||
                                           contentToDisplay.contains("<a", ignoreCase = true) ||
                                           contentToDisplay.contains("<h1", ignoreCase = true) ||
                                           contentToDisplay.contains("<h2", ignoreCase = true) ||
                                           contentToDisplay.contains("<h3", ignoreCase = true) ||
                                           contentToDisplay.contains("<h4", ignoreCase = true) ||
                                           contentToDisplay.contains("<h5", ignoreCase = true) ||
                                           contentToDisplay.contains("<h6", ignoreCase = true) ||
                                           contentToDisplay.contains("<img", ignoreCase = true) ||
                                           contentToDisplay.contains("<table", ignoreCase = true) ||
                                           contentToDisplay.contains("<ul", ignoreCase = true) ||
                                           contentToDisplay.contains("<ol", ignoreCase = true) ||
                                           contentToDisplay.contains("<li", ignoreCase = true) ||
                                           contentToDisplay.contains("<br", ignoreCase = true) ||
                                           contentToDisplay.contains("<hr", ignoreCase = true) ||
                                           contentToDisplay.contains("<b", ignoreCase = true) ||
                                           contentToDisplay.contains("<i", ignoreCase = true) ||
                                           contentToDisplay.contains("<strong", ignoreCase = true) ||
                                           contentToDisplay.contains("<em", ignoreCase = true) ||
                                           contentToDisplay.contains("<blockquote", ignoreCase = true) ||
                                           contentToDisplay.contains("<section", ignoreCase = true) ||
                                           contentToDisplay.contains("<article", ignoreCase = true))

                        android.util.Log.d("ArticleView", "isHtmlContent evaluation: $isHtmlContent")

                        if (isHtmlContent) {
                            android.util.Log.d("ArticleView", "Using WebView to display content.")
                            val webViewContext = LocalContext.current // Renamed to avoid shadowing
                            AndroidView(
                                factory = {
                                    WebView(webViewContext).apply {
                                        webViewClient = WebViewClient()
                                        settings.javaScriptEnabled = true // Enable if needed, be cautious with XSS
                                        settings.loadWithOverviewMode = true
                                        settings.useWideViewPort = true
                                        settings.builtInZoomControls = true
                                        settings.displayZoomControls = false
                                        // Adjust layout algorithm for mobile-friendly view
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                            settings.layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                                        }
                                    }
                                },
                                update = { webView ->
                                    // Determine base URL - use Wikipedia domain if it's Wikipedia content
                                    val baseUrl = if (uiState.fetchedWikipediaContent != null && uiState.event.contents.startsWith("https://") && uiState.event.contents.contains("wikipedia.org")) {
                                        val uri = java.net.URI(uiState.event.contents)
                                        "${uri.scheme}://${uri.host}"
                                    } else {
                                        "file:///android_asset/"
                                    }
                                    android.util.Log.d("ArticleView", "WebView Base URL: $baseUrl")
                                    webView.loadDataWithBaseURL(baseUrl, contentToDisplay, "text/html", "UTF-8", null)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 200.dp) // Ensure WebView has some minimum height
                            )
                        } else {
                            android.util.Log.d("ArticleView", "Using Text composable to display content.")
                            Text(
                                text = contentToDisplay,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
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
            icon = { Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = "Bình luận") }, // Corrected usage
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