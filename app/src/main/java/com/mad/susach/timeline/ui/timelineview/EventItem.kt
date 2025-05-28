package com.mad.susach.timeline.ui.timelineview

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.R
import com.mad.susach.event.data.Event
import com.mad.susach.article.ui.isNetworkUrl

@Composable
fun EventTimelineCircle(isSelected: Boolean, modifier: Modifier = Modifier) {
    val circleColor = colorResource(id = R.color.orange_icon)
    val outerSize = 26.dp
    val innerCircleSize = 16.dp
    val borderSize = 2.dp

    Box(
        modifier = modifier
            .size(outerSize)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Nếu được chọn, thêm vòng tròn bên ngoài
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(BorderStroke(borderSize, brush = SolidColor(circleColor)), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(innerCircleSize)
                    .background(color = circleColor, shape = CircleShape)
            )
        } else {
            // Nếu không được chọn, chỉ hiển thị hình tròn
            Box(
                modifier = Modifier
                    .size(innerCircleSize)
                    .background(color = circleColor, shape = CircleShape)
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
            
        //Hiệu ứng dropshadow
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = null
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp) 
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            EventTimelineCircle(
                isSelected = isSelected,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

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
                            color = if (isSelected)
                                colorResource(id = R.color.orange_text).copy(alpha = 0.8f)
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (isSelected) {
                        IconButton(onClick = { onNavigateToArticle(event.id) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Navigate to article",
                                tint = colorResource(id = R.color.orange_icon)
                            )
                        }
                    }
                }

                // Mở rộng nội dung khi được chọnchọn
                if (isSelected) {
                    EventImageSection(event)
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
                        )
                    }
                    // Summary section
                    if (event.summary.isNotBlank()) {
                        Spacer(
                            modifier = Modifier.height(
                                if (event.description.isNotBlank() || event.imageURL.isNotBlank()) 8.dp else 12.dp
                            )
                        )
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

// Hàm helper để kiểm tra xem URL có phải là URL mạng hay không
@Composable
private fun EventImageSection(event: Event) {
    val imageUrl = event.imageURL
    if (imageUrl.isNotBlank()) {
        val context = LocalContext.current
        val isNetwork = isNetworkUrl(imageUrl)
        if (isNetwork) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                contentDescription = event.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        } else {
            // Nếu không phải là URL mạng, sử dụng drawable resource
            val imageResId = remember(imageUrl, context) {
                val id = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
                if (id != 0) id else android.R.drawable.ic_menu_report_image
            }
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = event.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}


// Hàm helper để định dạng năm
fun formatEventDate(start: Int, end: Int): String {
    val startYear = if (start < 0) "${-start} TCN" else start.toString()
    val endYear = if (end < 0) "${-end} TCN" else end.toString()
    return if (start == end || end == 0) startYear else "$startYear - $endYear"
}
