package com.mad.susach.timeline.ui.timelineview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.susach.R
import com.mad.susach.event.data.model.Event
import com.mad.susach.event.ui.composables.EventItem
import com.mad.susach.timeline.data.model.Era
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreenUI(
    selectedEra: Era?,
    events: List<Event>,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit,
    selectedEventId: String?,
    onEventClick: (String) -> Unit,
    onNavigateToArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    eraIdForDisplay: String // Used to check if an era has been selected
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Standard AppBar height
                    .padding(top = 16.dp), // Add top padding
                contentAlignment = Alignment.Center
            ) {
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF222222)
                        )
                    }
                    Spacer(Modifier.weight(1f))

                    Text(
                        text = selectedEra?.name ?: "Timeline",
                        color = Color(0xFFFF6600),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.sitka_small_semibold)),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .padding(end = 48.dp) // Adjust padding to truly center if back button takes space
                    )
                    Spacer(Modifier.weight(1f)) // Ensure title is centered by balancing spacers
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply Scaffold's padding (e.g. for status bar)
                .padding(start = 30.dp, end = 8.dp) // Global start padding for all content below TopAppBar
        ) {
            if (eraIdForDisplay.isBlank()) {
                Text("Vui lòng chọn thời kì để xem timeline.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            } else if (error != null) {
                Text("Error: $error", modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else if (events.isEmpty()) {
                Text("No events available for this era.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
            } else {
                Box(modifier = Modifier.fillMaxSize()) { // Box to layer Line and LazyColumn
                    val lineThickness = 2.dp
                    // EventTimelineCircle is now always 26.dp wide. Its center is at 13.dp.
                    val lineStartX = (26.dp / 2) - (lineThickness / 2)

                    // Vertical Timeline - drawn behind the LazyColumn items
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = lineStartX) // Position line relative to the start of this Box
                            .width(lineThickness)
                            .background(Color(0xFFFF6600)) // Orange color for the line
                    )

                    val sortedEvents = events.sortedBy { it.startDate }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(sortedEvents, key = { it.id }) { event ->
                            EventItem(
                                event = event,
                                isSelected = event.id == selectedEventId,
                                onItemClick = { onEventClick(event.id) },
                                onNavigateToArticle = { articleId ->
                                    onNavigateToArticle(articleId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineScreen(
    eraId: String,
    navToArticle: (articleId: String) -> Unit,
    onBack: () -> Unit,
    viewModelFactory: androidx.lifecycle.ViewModelProvider.Factory? = null
) {
    val factory = viewModelFactory ?: remember(eraId) {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
                              @Suppress("UNCHECKED_CAST")
                    return TimelineViewModel(
                        repository = com.mad.susach.timeline.data.repository.TimelineRepository(), 
                        eraRepository = com.mad.susach.timeline.data.repository.EraRepository(), 
                        eraId = eraId
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
    val viewModel: TimelineViewModel = viewModel(factory = factory)

    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedEra by viewModel.selectedEra.collectAsState()

    var selectedEventId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(eraId) {
        if (eraId.isNotBlank()) {
            viewModel.loadEventsForEra(eraId)
            viewModel.loadEraDetails(eraId)
        }
    }

    TimelineScreenUI(
        selectedEra = selectedEra,
        events = events,
        isLoading = isLoading,
        error = error,
        onBack = onBack,
        selectedEventId = selectedEventId,
        onEventClick = { eventId ->
            selectedEventId = if (selectedEventId == eventId) null else eventId
        },
        onNavigateToArticle = navToArticle,
        eraIdForDisplay = eraId
    )
}


