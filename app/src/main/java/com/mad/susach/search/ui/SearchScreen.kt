package com.mad.susach.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    initialQuery: String = "",
    onBack: (String) -> Unit,
    onResultClick: (String) -> Unit, 
    viewModel: SearchViewModel = viewModel()
) {
    var query by remember { mutableStateOf(initialQuery) }
    val results by viewModel.results.collectAsState() 
    // val eras by viewModel.eras.collectAsState() // eras is directly accessed from viewModel.eras.value in FilterDropdown
    // val selectedEraId by remember { derivedStateOf { viewModel.selectedEraId } } // selectedEraId is directly accessed
    // val selectedSort by remember { derivedStateOf { viewModel.selectedSort } } // selectedSort is directly accessed

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFfffbff)) // Changed background color
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack(query) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.search(query)
                },
                placeholder = { Text("Tìm sự kiện...") },
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 44.dp)
                    .heightIn(min = 44.dp),
                singleLine = true,
                shape = RoundedCornerShape(22.dp), 
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = ""; viewModel.search("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                            }
                        }
                        IconButton(onClick = { viewModel.search(query) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFFF6600))
                        }
                    }
                }
            )
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 4.dp) 
            ) {
                FilterDropdown(
                    label = "Thời đại:",
                    selectedValue = viewModel.eras.value.find { it.id == viewModel.selectedEraId }?.name ?: "Tất cả",
                    items = listOf("Tất cả") + viewModel.eras.value.map { it.name },
                    onItemSelected = {
                        if (it == "Tất cả") {
                            viewModel.setSelectedEra(null)
                        } else {
                            val selectedEraObject = viewModel.eras.value.find { era -> era.name == it }
                            viewModel.setSelectedEra(selectedEraObject?.id)
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 4.dp) 
            ) {
                FilterDropdown(
                    label = "Sắp xếp:",
                    selectedValue = viewModel.selectedSort, // Directly use viewModel.selectedSort
                    items = listOf("A-Z", "Mới nhất", "Xưa nhất"),
                    onItemSelected = { viewModel.setSort(it) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (results.isEmpty() && query.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Không tìm thấy sự kiện nào.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp), 
                modifier = Modifier.fillMaxSize() 
            ) {
                items(results, key = { event -> event.id }) { event ->
                    SearchResultItem(
                        event = event,
                        onItemClick = { eventId -> onResultClick(eventId) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(
    label: String,
    selectedValue: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            label,
            color = Color(0xFFFF6600),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            Text(
                selectedValue,
                color = Color.Black,
                fontSize = 16.sp,
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.45f) 
            ) {
                items.forEach { item -> 
                    DropdownMenuItem(
                        text = { Text(item, fontSize = 16.sp) }, 
                        onClick = {
                            onItemSelected(item) 
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    // If SuSachTheme is available, wrap SearchScreen with it.
    // MaterialTheme { // Using MaterialTheme as a placeholder if SuSachTheme is not found
        SearchScreen(
            initialQuery = "Test Query",
            onBack = { queryString -> println("Back clicked with query: $queryString") }, // Named lambda param
            onResultClick = { eventId -> println("Result clicked: $eventId") } // Named lambda param
        )
    // }
}
