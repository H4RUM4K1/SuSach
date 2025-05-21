package com.mad.susach.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mad.susach.article.ui.isNetworkUrl
import com.mad.susach.event.data.Event
import androidx.compose.ui.tooling.preview.Preview // Added for Preview
import androidx.compose.material3.Surface // Added for Preview Surface

@Composable
fun SearchResultItem(
    event: Event,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onItemClick(event.id) },
        shape = MaterialTheme.shapes.extraSmall, // Use a non-rounded shape or RectangleShape
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text content first
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${event.startDate}${if (event.endDate != 0 && event.endDate != event.startDate) " - ${event.endDate}" else ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Image at the end
            if (isNetworkUrl(event.imageURL)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(event.imageURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = event.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop,
                    onError = { /* Handle error if needed */ },
                    onSuccess = { /* Handle success if needed */ }
                )
            } else if (event.imageURL.isNotEmpty()) { // Local drawable resource
                val context = LocalContext.current
                val resourceId = remember(event.imageURL) {
                    context.resources.getIdentifier(event.imageURL, "drawable", context.packageName)
                }
                if (resourceId != 0) {
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = event.name,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Spacer(modifier = Modifier.size(50.dp))
                }
            } else {
                Spacer(modifier = Modifier.size(50.dp))
            }
        }
    }
}
