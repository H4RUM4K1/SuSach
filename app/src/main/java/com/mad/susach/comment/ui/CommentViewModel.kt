package com.mad.susach.comment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mad.susach.comment.data.Comment
import com.mad.susach.comment.data.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CommentUiState(
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CommentViewModel(
    private val repository: CommentRepository = CommentRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommentUiState())
    val uiState: StateFlow<CommentUiState> = _uiState

    fun loadComments(eventId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getCommentsForEvent(eventId).collect { comments ->
                    _uiState.value = CommentUiState(
                        comments = comments,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun addComment(eventId: String, content: String) {
        viewModelScope.launch {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(error = "Please login to comment")
                    return@launch
                }

                // Get username from email
                val userName = currentUser.email?.substringBefore("@") ?: "Anonymous"

                val comment = Comment(
                    id = "", // Will be set by repository
                    eventId = eventId,
                    userId = currentUser.uid,
                    userName = userName,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )

                repository.addComment(comment)
                loadComments(eventId) // Reload comments after adding
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
