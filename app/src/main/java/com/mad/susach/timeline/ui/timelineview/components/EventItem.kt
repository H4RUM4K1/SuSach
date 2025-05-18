package com.mad.susach.timeline.ui.timelineview.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.event.data.model.Event

@Composable
fun EventItem(
    event: Event,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClick(event.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for image - replace with actual image loading
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(event.imageURL) // Use event.imageUrl if available, otherwise a placeholder
                    .crossfade(true)
                    .build(),
                placeholder = null, // Replace with your placeholder
                contentDescription = event.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatEventDate(event.startDate, event.endDate),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3 // Show a snippet of the description
                )
            }
        }
    }
}

// Helper function for formatting event dates
fun formatEventDate(start: Int, end: Int): String = if (start == end || end == 0) start.toString() else "$start - $end"

// @Preview(showBackground = true)
// @Composable
// fun EventItemPreview() {
//    val sampleEvent = Event(
//        id = "1",
//        eraId = "era1",
//        title = "Major Historical Event",
//        date = "January 1, 1000",
//        description = "This is a detailed description of a major historical event that had significant impact.",
//        imageUrl = "", // Add a sample image URL or leave empty for placeholder
//        articleIds = listOf("article1")
//    )
//    EventItem(event = sampleEvent, onItemClick = {})
// }
