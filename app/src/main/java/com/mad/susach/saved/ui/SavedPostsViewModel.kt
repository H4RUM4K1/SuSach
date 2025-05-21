package com.mad.susach.saved.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.saved.data.SavedPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedPostsViewModel(
    private val repository: SavedPostRepository = SavedPostRepository()
) : ViewModel() {
    data class SavedPostsUiState(
        val savedEvents: List<Event> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(SavedPostsUiState())
    val uiState: StateFlow<SavedPostsUiState> = _uiState

    init {
        loadSavedPosts()
    }

    private fun loadSavedPosts() {
        viewModelScope.launch {
            try {
                val savedPosts = repository.getSavedPosts()
                _uiState.value = SavedPostsUiState(
                    savedEvents = savedPosts,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = SavedPostsUiState(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}
