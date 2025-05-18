package com.mad.susach.timeline.ui.timelineview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mad.susach.timeline.ui.eralselection.components.EraItem
import com.mad.susach.timeline.ui.eralselection.EraSelectionViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    eraId: String,
    navToEventDetail: (eventId: String) -> Unit,
    viewModel: TimelineViewModel = viewModel()
) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedEra by viewModel.selectedEra.collectAsState()

    LaunchedEffect(eraId) {
        viewModel.loadEventsForEra(eraId)
        viewModel.loadEraDetails(eraId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(selectedEra?.name ?: "Timeline") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                Text("Loading events...")
            } else if (error != null) {
                Text("Error: $error")
            } else if (events.isEmpty()) {
                Text("No events available for this era.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(events) { event ->
                        EventItem(event = event, onItemClick = { navToEventDetail(event.id) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EraSelectionScreen(
    viewModel: EraSelectionViewModel = viewModel(),
    navToTimeline: (eraId: String) -> Unit
) {
    val eras by viewModel.eras.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navToTimeline("") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF222222)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Chọn thời kì",
                        color = Color(0xFFFF6600),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.sitka_small_semibold)),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 48.dp)
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                eras.isEmpty() -> {
                    Text(
                        text = "No eras found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(eras.size) { idx ->
                            EraItem(era = eras[idx], onEraClick = { navToTimeline(it) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}


