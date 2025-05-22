package com.mad.susach.comment.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.susach.comment.data.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    eventId: String,
    onBack: () -> Unit,
    viewModel: CommentViewModel = viewModel()
) {
    var commentText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadComments(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comments") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Comment input
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                label = { Text("Add a comment") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Button(
                onClick = {
                    if (commentText.isNotBlank()) {
                        viewModel.addComment(eventId, commentText)
                        commentText = ""
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Post")
            }

            // Error message
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Comments list
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(androidx.compose.ui.Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(uiState.comments) { comment ->
                        CommentItem(comment)
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = comment.userName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
