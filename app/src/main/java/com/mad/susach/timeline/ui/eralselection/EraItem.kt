package com.mad.susach.timeline.ui.eralselection

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.susach.R
import com.mad.susach.timeline.data.Era

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
            .then(
                if (isSelected)
                    Modifier.border(2.dp, Color(0xFF2196F3), RoundedCornerShape(12.dp))
                else Modifier
            ),

        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(96.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ảnh minh họa
            val imageResId = getEraImageResId(era.imageUrl)

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = era.name,
                modifier = Modifier
                    .height(78.dp)
                    .width(78.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Chi tiết
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

// Hàm helper để lấy image từ url
@SuppressLint("DiscouragedApi")
@Composable
private fun getEraImageResId(imageUrl: String): Int {
    val context = LocalContext.current
    return remember(imageUrl) {
        if (imageUrl.isNotBlank()) {
            val resId = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
            if (resId == 0) R.drawable.ic_launcher_background else resId
        } else {
            R.drawable.ic_launcher_background
        }
    }
}

// Hàm helper định dạng năm TCN
fun formatEraYear(year: Int): String {
    return when {
        year < 0 -> "${-year} TCN"
        year == 0 -> "0"
        else -> year.toString()
    }
}

