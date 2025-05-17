package com.mad.susach.timeline.ui.timelineview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.susach.event.data.model.Event
import com.mad.susach.event.ui.composables.EventItem

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

// Preview for TimelineScreen (Optional)
// @Preview(showBackground = true)
// @Composable
// fun TimelineScreenPreview() {
//    // Similar to EraSelectionScreen, providing a mock ViewModel or data is needed.
//    // TimelineScreen(eraId = "preview_era_id", navToEventDetail = {})
// }
