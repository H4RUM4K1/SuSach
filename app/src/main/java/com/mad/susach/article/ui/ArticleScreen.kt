package com.mad.susach.article.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    articleId: String,
    navController: androidx.navigation.NavController, // Added NavController for back navigation
    viewModel: ArticleViewModel = viewModel()
) {
    val article by viewModel.article.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(articleId) {
        viewModel.loadArticle(articleId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(article?.title ?: "Article") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                Text("Loading article...") // Replace with a proper loading indicator
            } else if (error != null) {
                Text("Error: $error") // Replace with a proper error message
            } else if (article == null) {
                Text("Article not found.")
            } else {
                article?.let { art ->
                    // Display multiple images if available
                    art.imageUrls?.forEach { imageUrl ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            // You might want a generic placeholder or one specific to articles
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = art.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp) // Adjust as needed
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = art.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = art.content, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    art.source?.let {
                        Text(text = "Source: $it", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
