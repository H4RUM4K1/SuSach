package com.mad.susach.timeline.ui.eralselection

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import com.mad.susach.timeline.ui.eralselection.components.EraItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import com.mad.susach.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.wrapContentSize
import com.mad.susach.timeline.data.model.Era

@Composable
fun EraSelectionScreen(
    onBack: (String) -> Unit,
    navToTimeline: (eraId: String) -> Unit,
    viewModel: EraSelectionViewModel = viewModel()
) {
    val eras by viewModel.eras.collectAsState()
    val sortedEras = eras.sortedBy { it.startYear }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

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
                    IconButton(onClick = { onBack("") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF222222)
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
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .padding(end = 48.dp)
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                    )
                }
                eras.isEmpty() -> {
                    Text(
                        text = "No eras found.",
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(sortedEras.size) { idx ->
                            EraItem(era = sortedEras[idx], onEraClick = { navToTimeline(it) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EraSelectionScreenPreview() {
    val fakeEras = listOf(
        Era(id = "1", name = "Thời kỳ cổ đại", description = "Thời kỳ dựng nước đầu tiên của người Việt.", startYear = -2879, endYear = -179),
        Era(id = "2", name = "Thời kỳ Bắc thuộc", description = null, startYear = -179, endYear = 939),
        Era(id = "3", name = "Thời kỳ Quân chủ", description = null, startYear = 939, endYear = 1945),
        Era(id = "4", name = "Thời kỳ hiện đại", description = null, startYear = 1858, endYear = 2025)
    )
    MaterialTheme {
        Surface(color = Color(0xFFFDF6F0)) {
            Column(Modifier.fillMaxSize().padding(8.dp)) {
                Text(
                    text = "Chọn thời kì",
                    color = Color(0xFFFF6600),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
                )
                LazyColumn {
                    items(fakeEras.size) { idx ->
                        EraItem(era = fakeEras[idx], onEraClick = {}, isSelected = idx == 0)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
