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
import com.mad.susach.event.data.Event
import com.mad.susach.timeline.data.Era
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mad.susach.timeline.data.EraRepository

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
    eraIdForDisplay: String 
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
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
                            .padding(end = 48.dp)
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 30.dp, end = 8.dp)
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
                Box(modifier = Modifier.fillMaxSize()) {
                    val lineThickness = 2.dp
                    val lineStartX = (26.dp / 2) - (lineThickness / 2)
                    // Vertical line for timeline
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = lineStartX)
                            .width(lineThickness)
                            .background(Color(0xFFFF6600))
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
    viewModelFactory: androidx.lifecycle.ViewModelProvider.Factory? = null // Parameter can be kept for testing or specific overrides
) {
    val factory = viewModelFactory ?: remember(eraId) {
        TimelineViewModel.provideFactory(
            eraRepository = EraRepository(),       // Consider using DI
            eraId = eraId
        )
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


