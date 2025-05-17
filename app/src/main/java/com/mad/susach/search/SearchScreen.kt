package com.mad.susach.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Search
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Box(modifier = Modifier.weight(1f)) {
                TextField(
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
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFFF6600),
                        unfocusedIndicatorColor = Color(0xFFFF6600)
                    ),
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
        Spacer(Modifier.height(12.dp))
        Row {
            Text("Tìm kiếm:", color = Color(0xFFFF6600), fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Text("Bất kì", color = Color.Black)
            Spacer(Modifier.width(16.dp))
            Text("Sắp xếp:", color = Color(0xFFFF6600), fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Text("Lượt xem", color = Color.Black)
        }
        Spacer(Modifier.height(12.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            LazyColumn {
                items(results) { item ->
                    SearchResultRow(item, onResultClick)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFFF6600))
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
            .clickable { onClick(item) }
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(item.year, color = Color.Gray, fontSize = 13.sp)
        }
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFFF6600))
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.NorthEast, contentDescription = null, tint = Color.Black)
    }
}

@Preview(showBackground = true, name = "SearchScreen Preview")
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        initialQuery = "",
        onBack = {},
        onResultClick = {},
        viewModel = SearchViewModel()
    )
}
