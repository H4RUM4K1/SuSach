package com.mad.susach.timeline.ui.timelineview

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image // Added import for Image
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
import androidx.compose.runtime.remember // Added import for remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource // Added for painterResource
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.susach.R // Import R class for color resources
import com.mad.susach.event.data.Event

@Composable
fun EventTimelineCircle(isSelected: Boolean, modifier: Modifier = Modifier) {
    val circleColor = colorResource(id = R.color.orange_icon) // Use color from XML
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
                    .border(BorderStroke(borderSize, brush = SolidColor(circleColor)), CircleShape) // Explicitly use SolidColor for brush
            )
            // Inner circle, centered within the outerSize
            Box(
                modifier = Modifier
                    .size(innerCircleSize)
                    .background(color = circleColor, shape = CircleShape) // Explicitly use color parameter
            )
        } else {
            // Default solid circle, centered within the outerSize
            Box(
                modifier = Modifier
                    .size(innerCircleSize)
                    .background(color = circleColor, shape = CircleShape) // Explicitly use color parameter
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
                            color = if (isSelected) colorResource(id = R.color.orange_text) else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formatEventDate(event.startDate, event.endDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) colorResource(id = R.color.orange_text).copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (isSelected) {
                        IconButton(onClick = { onNavigateToArticle(event.id) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Navigate to article",
                                tint = colorResource(id = R.color.orange_icon) // Use color from XML
                            )
                        }
                    }
                }

                if (isSelected) {
                    // Image display section - only shown if imageURL is not blank AND item is selected
                    if (event.imageURL.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp)) // Add some space before the image
                        val context = LocalContext.current
                        val imageResId = remember(event.imageURL, context) {
                            val id = context.resources.getIdentifier(event.imageURL, "drawable", context.packageName)
                            if (id != 0) id else android.R.drawable.ic_menu_gallery // Fallback placeholder
                        }

                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = event.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp) // You can adjust the height as needed
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }

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
                        Spacer(modifier = Modifier.height(if (event.description.isNotBlank() || event.imageURL.isNotBlank()) 8.dp else 12.dp)) // Adjust spacer
                        Text(
                            text = "Tóm tắt:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = event.summary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

fun formatEventDate(start: Int, end: Int): String {
    val startYear = if (start < 0) "${-start} TCN" else start.toString()
    val endYear = if (end < 0) "${-end} TCN" else end.toString()
    return if (start == end || end == 0) startYear else "$startYear - $endYear"
}
