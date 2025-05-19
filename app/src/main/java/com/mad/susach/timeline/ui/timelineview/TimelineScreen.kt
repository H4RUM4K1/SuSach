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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    navToEventDetail: (eventId: String) -> Unit
) {
    val factory = remember(eraId) {
        TimelineViewModelFactory(eraId = eraId)
    }
    val viewModel: TimelineViewModel = viewModel(factory = factory)
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedEra by viewModel.selectedEra.collectAsState()

    // Only load if eraId is not blank
    LaunchedEffect(eraId) {
        if (eraId.isNotBlank()) {
            viewModel.loadEventsForEra(eraId)
            viewModel.loadEraDetails(eraId)
        }
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
            if (eraId.isBlank()) {
                Text("Vui lòng chọn thời kì để xem timeline.")
            } else if (isLoading) {
                Text("Loading events...")
            } else if (error != null) {
                Text("Error: $error")
            } else if (events.isEmpty()) {
                Text("No events available for this era.")
            } else {
                val sortedEvents = events.sortedBy { it.startDate }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(sortedEvents) { event ->
                        EventItem(event = event, onItemClick = { navToEventDetail(event.id) })
                    }
                }
            }
        }
    }
}

