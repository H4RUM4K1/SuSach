package com.mad.susach.timeline.ui.eralselection

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
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Chọn thời kì",
                            color = Color(0xFFFF6600),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.sitka_small_semibold))
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navToTimeline("") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFF6600)
                        )
                    }
                }
            )
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
                                    fontSize = 20.sp
                                )
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

@Preview(showBackground = true, name = "EraSelectionScreen Preview")
@Composable
fun EraSelectionScreenPreview() {
    EraSelectionScreen(navToTimeline = {})
}
