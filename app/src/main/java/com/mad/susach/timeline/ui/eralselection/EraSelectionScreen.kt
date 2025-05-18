package com.mad.susach.timeline.ui.eralselection

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.susach.R
import com.mad.susach.timeline.data.model.Era
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EraSelectionScreen(
    navToTimeline: (eraId: String) -> Unit,
    viewModel: EraSelectionViewModel = viewModel()
) {
    val eras = listOf(
        Era(id = "ancient", name = "Thời kỳ cổ đại", description = null, startYear = 2879, endYear = -179),
        Era(id = "bac_thuoc", name = "Thời kỳ Bắc thuộc", description = null, startYear = -179, endYear = 939),
        Era(id = "quan_chu", name = "Thời kỳ Quân chủ", description = null, startYear = 939, endYear = 1945),
        Era(id = "modern", name = "Thời kỳ hiện đại", description = null, startYear = 1858, endYear = 9999)
    )
    val eraImages = listOf(
        R.mipmap.ic_launcher_foreground, // ancient
        R.mipmap.ic_launcher_foreground, // bac_thuoc
        R.mipmap.ic_launcher_foreground, // quan_chu
        R.mipmap.ic_launcher_foreground  // modern
    )
    val eraPeriods = listOf(
        "(2879 - 179 TCN)",
        "(179 TCN - 939 SCN)",
        "(939 - 1945)",
        "(1858 - nay)"
    )
    val eraNames = listOf(
        "Thời kỳ cổ đại",
        "Thời kỳ Bắc thuộc",
        "Thời kỳ Quân chủ",
        "Thời kỳ hiện đại"
    )
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Standard AppBar height
                    .padding(top = 16.dp), // Add top padding
                contentAlignment = Alignment.Center
            ) {
                // Back arrow on the left
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navToTimeline("") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF222222) // Black-grey, same as SearchScreen
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(eras.size) { idx ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(vertical = 8.dp)
                            .clickable(enabled = eras[idx].id == "bac_thuoc") { navToTimeline(eras[idx].id) },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = eraImages[idx]),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(70.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    eraNames[idx],
                                    color = Color(0xFFFF6600),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.sitka_small_semibold))
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    eraPeriods[idx],
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Timeline event data class
data class TimelineEvent(val year: String, val title: String, val isMain: Boolean = false)

fun sampleTimelineEvents() = listOf(
    TimelineEvent("Thế Kỷ VII TCN", "Nước Văn Lang", true),
    TimelineEvent("258 TCN", "Nước Âu Lạc"),
    TimelineEvent("218 TCN", "Chiến tranh Tần - Việt"),
    TimelineEvent("204 TCN", "Bắc Thuộc - Nhà Triệu\n(Nam Việt)"),
    TimelineEvent("111 TCN", "Bắc Thuộc - Nhà Hán\n(Giao Chỉ)")
)

@Composable
fun TimelineScreen(
    onBack: () -> Unit = {},
    title: String = "Thời kì Bắc thuộc",
    period: String = "(179 TCN - 939 SCN)",
    events: List<TimelineEvent> = sampleTimelineEvents(),
    mainImageRes: Int = R.mipmap.ic_launcher_foreground
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6F0))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF222222)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = Color(0xFFFF6600),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.sitka_small_semibold))
                )
                Text(
                    text = period,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.width(48.dp))
        }
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            // Timeline line
            Canvas(
                Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 24.dp, bottom = 24.dp)
            ) {
                drawLine(
                    color = Color(0xFFFF6600),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, size.height),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            // Timeline events
            Column(
                Modifier
                    .padding(start = 0.dp, top = 0.dp, bottom = 0.dp)
            ) {
                events.forEachIndexed { idx, event ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = if (idx == events.lastIndex) 0.dp else 24.dp)
                    ) {
                        // Dot
                        Box(
                            Modifier
                                .size(if (event.isMain) 24.dp else 14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF6600))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = event.year,
                                color = Color(0xFFFF6600),
                                fontWeight = FontWeight.Bold,
                                fontSize = if (event.isMain) 16.sp else 15.sp
                            )
                            Text(
                                text = event.title,
                                color = if (event.isMain) Color(0xFFFF6600) else Color.Black,
                                fontWeight = if (event.isMain) FontWeight.Bold else FontWeight.Normal,
                                fontSize = if (event.isMain) 17.sp else 15.sp
                            )
                        }
                        // Main image for the first event
                        if (event.isMain) {
                            Spacer(Modifier.width(16.dp))
                            Image(
                                painter = painterResource(id = mainImageRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "EraSelectionScreen Preview")
@Composable
fun EraSelectionScreenPreview() {
    EraSelectionScreen(navToTimeline = {})
}

@Preview(showBackground = true, name = "TimelineScreen Preview")
@Composable
fun TimelineScreenPreview() {
    TimelineScreen()
}
