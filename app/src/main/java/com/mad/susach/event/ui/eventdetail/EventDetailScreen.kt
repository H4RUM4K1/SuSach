package com.mad.susach.event.ui.eventdetail

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.R // Placeholder
import com.mad.susach.navigation.Screen // For navigation to article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    navController: NavController,
    viewModel: EventDetailViewModel = viewModel() 
) {
    val eventDetail by viewModel.eventDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(eventDetail?.name ?: "Event Detail") }, // Changed from .title
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
                Text("Loading event details...")
            } else if (error != null) {
                Text("Error: $error")
            } else if (eventDetail == null) {
                Text("Event not found.")
            } else {
                eventDetail?.let { event ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(event.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = event.name, // Changed from .title
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth() // Changed from fillMaxSize
                            .height(250.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = event.name, style = MaterialTheme.typography.headlineSmall) // Changed from .title
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Date: ${event.date}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = event.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Navigation to articles
                    if (event.articleId.isNotEmpty()) {
                        Text("Related Article:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { navController.navigate(Screen.ArticleDetail.createRoute(event.articleId)) }) {
                            Text("Read Article: ${event.articleId}")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "EventDetailScreen Preview")
@Composable
fun EventDetailScreenPreview() {
    EventDetailScreen(
        eventId = "sample_event_id",
        navController = rememberNavController()
    )
}
