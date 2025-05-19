package com.mad.susach.event.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.event.data.model.Event

@Composable
fun EventTimelineCircle(isSelected: Boolean, modifier: Modifier = Modifier) {
    val circleColor = Color(0xFFFF6600) // Orange
    val outerSize = 26.dp // Consistent outer size for layout calculations and alignment
    val innerCircleSize = 16.dp
    val borderSize = 2.dp
    // val paddingBetweenCircleAndBorder = 3.dp // Derived: (26 - 16 - 2*2)/2 = 3

    Box(
        modifier = modifier
            .size(outerSize) // Occupy a consistent space for alignment purposes
            .clip(CircleShape), // Clip potential border drawn directly on this Box if structure changes
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            // Outer border, drawn on a Box that fills the outerSize
            Box(
                modifier = Modifier
                    .matchParentSize() // Fills the 26.dp outer size
                    .border(BorderStroke(borderSize, circleColor), CircleShape)
            )
            // Inner circle, centered within the outerSize
            Box(
                modifier = Modifier
                    .size(innerCircleSize)
                    .background(circleColor, CircleShape)
            )
        } else {
            // Default solid circle, centered within the outerSize
            Box(
                modifier = Modifier
                    .size(innerCircleSize)
                    .background(circleColor, CircleShape)
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    isSelected: Boolean,
    onItemClick: (String) -> Unit,
    onNavigateToArticle: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize()
            .clickable { onItemClick(event.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = null
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp) // Horizontal padding removed, handled by TimelineScreenUI
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            EventTimelineCircle(
                isSelected = isSelected,
                modifier = Modifier.padding(top = 4.dp) // Y-axis padding for visual alignment with text
            )
            Spacer(modifier = Modifier.width(12.dp)) // Space between circle and text content
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = if (isSelected) 18.sp else 16.sp
                            ),
                            color = if (isSelected) Color(0xFFD95500) else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formatEventDate(event.startDate, event.endDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) Color(0xFFD95500).copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (isSelected) {
                        IconButton(onClick = { onNavigateToArticle(event.id) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Navigate to article",
                                tint = Color(0xFFFF6600) // Changed to orange Color(0xFFFF6600)
                            )
                        }
                    }
                }

                if (isSelected) {
                    // Conditionally display Description section
                    if (event.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mô tả:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 5 // Limit lines for description
                        )
                    }

                    // Conditionally display Summary section
                    if (event.summary.isNotBlank()) {
                        Spacer(modifier = Modifier.height(if (event.description.isNotBlank()) 8.dp else 12.dp)) // Adjust spacer based on description presence
                        Text(
                            text = "Tóm tắt:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = event.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3 // Limit lines for summary
                        )
                    }

                    if (event.imageURL.isNotBlank()) {
                        // Adjust spacer based on description or summary presence
                        val spacerHeight = if (event.description.isNotBlank() || event.summary.isNotBlank()) 8.dp else 12.dp
                        Spacer(modifier = Modifier.height(spacerHeight))
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(event.imageURL)
                                .crossfade(true)
                                .build(),
                            placeholder = null,
                            contentDescription = event.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(top = 8.dp)
                                .clip(MaterialTheme.shapes.medium) // Clip image corners
                        )
                    }
                }
            }
        }
    }
}

// Helper function for formatting event dates
fun formatEventDate(start: Int, end: Int): String {
    val startYear = if (start < 0) "${-start} TCN" else start.toString()
    val endYear = if (end < 0) "${-end} TCN" else end.toString()
    return if (start == end || end == 0) startYear else "$startYear - $endYear"
}
