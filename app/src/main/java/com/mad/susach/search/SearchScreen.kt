package com.mad.susach.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    initialQuery: String = "",
    onBack: (String) -> Unit,
    onResultClick: (SearchResult) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    var query by remember { mutableStateOf(initialQuery) }
    val results by viewModel.results.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6F0))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack(query) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.search(query)
                    },
                    placeholder = { Text("Tìm gì đó...") },
                    modifier = Modifier
                        .fillMaxWidth()
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
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color(0xFFFDF6F0)), // Match background
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tìm kiếm box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFDF6F0), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Tìm kiếm:",
                        color = Color(0xFFFF6600),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    // Dropdown for filter
                    var expanded by remember { mutableStateOf(false) }
                    val selectedType = viewModel.selectedType
                    Box {
                        Text(
                            selectedType,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clickable { expanded = true }
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(text = { Text("Bất kì", fontSize = 16.sp) }, onClick = { viewModel.setType("Bất kì"); expanded = false })
                            DropdownMenuItem(text = { Text("Sự kiện", fontSize = 16.sp) }, onClick = { viewModel.setType("Sự kiện"); expanded = false })
                            DropdownMenuItem(text = { Text("Trắc nghiệm", fontSize = 16.sp) }, onClick = { viewModel.setType("Trắc nghiệm"); expanded = false })
                        }
                    }
                }
            }
            Spacer(Modifier.width(12.dp))
            // Sắp xếp box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFDF6F0), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Sắp xếp:",
                        color = Color(0xFFFF6600),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    // Dropdown for sort
                    var expandedSort by remember { mutableStateOf(false) }
                    val selectedSort = viewModel.selectedSort
                    Box {
                        Text(
                            selectedSort,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clickable { expandedSort = true }
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expandedSort,
                            onDismissRequest = { expandedSort = false }
                        ) {
                            DropdownMenuItem(text = { Text("A-Z", fontSize = 16.sp) }, onClick = { viewModel.setSort("A-Z"); expandedSort = false })
                            DropdownMenuItem(text = { Text("Mới nhất", fontSize = 16.sp) }, onClick = { viewModel.setSort("Mới nhất"); expandedSort = false })
                            DropdownMenuItem(text = { Text("Xưa nhất", fontSize = 16.sp) }, onClick = { viewModel.setSort("Xưa nhất"); expandedSort = false })
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Card(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Box(Modifier.background(Color.White)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(results) { item ->
                        SearchResultRow(item, onResultClick)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFFF6600))
            Spacer(Modifier.width(4.dp))
            Text("Theo vào timeline", color = Color(0xFFFF6600))
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Default.NorthEast, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(4.dp))
            Text("Truy cập bài viết", color = Color.Black)
        }
    }
}

@Composable
fun SearchResultRow(item: SearchResult, onClick: (SearchResult) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(58.dp) // 1.2x of previous ~48dp
            .clickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(top = 6.dp, bottom = 6.dp, start = 12.dp) // 6dp padding inside text column
        ) {
            Text(
                item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFFFF6600) // Orange title
            )
            Text(item.year, color = Color.Gray, fontSize = 13.sp)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFFF6600))
        Spacer(Modifier.width(8.dp))
        Icon(
            Icons.Default.NorthEast,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.padding(end = 12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "SearchScreen Preview")
@Composable
fun SearchScreenPreview() {
    val fakeResults = listOf(
        SearchResult(title = "Nhà Đinh", year = "968 - 980"),
        SearchResult(title = "Nhà Tiền Lê", year = "980 - 1009"),
        SearchResult(title = "Nhà Lý", year = "1009 - 1225")
    )
    SearchScreenFake(
        initialQuery = "",
        onBack = {},
        onResultClick = {},
        results = fakeResults
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenFake(
    initialQuery: String = "",
    onBack: (String) -> Unit,
    onResultClick: (SearchResult) -> Unit,
    results: List<SearchResult>
) {
    var query by remember { mutableStateOf(initialQuery) }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6F0))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack(query) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Tìm gì đó...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 44.dp)
                        .heightIn(min = 44.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    // colors = OutlinedTextFieldDefaults.textFieldColors(
                    //     focusedBorderColor = Color(0xFFFF6600),
                    //     unfocusedBorderColor = Color(0x33FF6600),
                    //     containerColor = Color.White
                    // ),
                    // Compose Material3 1.2.1 does not support custom colors for OutlinedTextField via OutlinedTextFieldDefaults. Use default colors or update Compose version for color customization.
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                                }
                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFFF6600))
                            }
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color(0xFFFDF6F0)), // Match background
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tìm kiếm box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFDF6F0), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Tìm kiếm:",
                        color = Color(0xFFFF6600),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    // Dropdown for filter
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Text(
                            "Bất kì",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clickable { expanded = true }
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(text = { Text("Bất kì", fontSize = 16.sp) }, onClick = { expanded = false })
                            DropdownMenuItem(text = { Text("Sự kiện", fontSize = 16.sp) }, onClick = { expanded = false })
                            DropdownMenuItem(text = { Text("Trắc nghiệm", fontSize = 16.sp) }, onClick = { expanded = false })
                        }
                    }
                }
            }


            Spacer(Modifier.width(12.dp))


            // Sắp xếp box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFDF6F0), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Sắp xếp:",
                        color = Color(0xFFFF6600),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    // Dropdown for sort
                    var expandedSort by remember { mutableStateOf(false) }
                    Box {
                        Text(
                            "A-Z",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clickable { expandedSort = true }
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expandedSort,
                            onDismissRequest = { expandedSort = false }
                        ) {
                            DropdownMenuItem(text = { Text("A-Z", fontSize = 16.sp) }, onClick = { expandedSort = false })
                            DropdownMenuItem(text = { Text("Mới nhất", fontSize = 16.sp) }, onClick = { expandedSort = false })
                            DropdownMenuItem(text = { Text("Xưa nhất", fontSize = 16.sp) }, onClick = { expandedSort = false })

                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Card(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Box(Modifier.background(Color.White)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(results) { item ->
                        SearchResultRow(item, onResultClick)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFFF6600))
            Spacer(Modifier.width(4.dp))
            Text("Theo vào timeline", color = Color(0xFFFF6600))
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Default.NorthEast, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(4.dp))
            Text("Truy cập bài viết", color = Color.Black)
        }
    }
}
