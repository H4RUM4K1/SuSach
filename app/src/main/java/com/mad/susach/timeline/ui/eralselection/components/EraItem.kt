package com.mad.susach.timeline.ui.eralselection.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.mad.susach.R
import com.mad.susach.timeline.data.model.Era

@Composable
fun EraItem(
    era: Era,
    onEraClick: (String) -> Unit,
    isSelected: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onEraClick(era.id) }
            .then(if (isSelected) Modifier.border(2.dp, Color(0xFF2196F3), RoundedCornerShape(12.dp)) else Modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Era image (placeholder if none)
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = era.name,
                    color = Color(0xFFFF6600),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "(${formatEraYear(era.startYear)} - ${formatEraYear(era.endYear)})",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
                era.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

fun formatEraYear(year: Int): String {
    return if (year < 0) "${-year} TCN" else if (year == 0) "0" else year.toString()
}

@Preview(showBackground = true)
@Composable
fun EraItemPreview() {
    EraItem(
        era = Era(
            id = "1",
            name = "Thời kỳ cổ đại",
            description = "Thời kỳ dựng nước đầu tiên của người Việt.",
            startYear = -2879,
            endYear = -179
        ),
        onEraClick = {},
        isSelected = true
    )
}
