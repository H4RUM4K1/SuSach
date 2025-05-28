package com.mad.susach.timeline.ui.timelineview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.susach.R
import com.mad.susach.event.data.Event
import com.mad.susach.timeline.data.Era
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
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TimelineTopBar(
                title = selectedEra?.name ?: "Timeline",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 30.dp, end = 8.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
                error != null -> {
                    Text(
                        "Error: $error",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
                events.isEmpty() -> {
                    Text(
                        "Thời kì này không có sự kiện nào.",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Đường timelinee
                        val lineStartX = 12.dp
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = lineStartX)
                                .width(2.dp)
                                .background(Color(0xFF000000))
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
}

//TopbarTopbar
@Composable
private fun TimelineTopBar(title: String, onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF222222)
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = title,
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

@Composable
fun TimelineScreen(
    eraId: String,
    navToArticle: (articleId: String) -> Unit,
    onBack: () -> Unit,
    viewModelFactory: androidx.lifecycle.ViewModelProvider.Factory? = null
) {
    val factory = viewModelFactory ?: remember(eraId) {
        TimelineViewModel.provideFactory(
            eraRepository = EraRepository(),
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
    )
}


