package com.mad.susach.article.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.event.data.EventRepository
import com.mad.susach.saved.data.SavedPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArticleUiState(
    val isLoading: Boolean = true,
    val event: Event? = null,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

class ArticleViewModel(
    private val repository: EventRepository = EventRepository(),
    private val savedPostRepository: SavedPostRepository = SavedPostRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

    fun fetchEvent(eventId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ArticleUiState(isLoading = true)
                val event = repository.getEventById(eventId)
                if (event != null) {
                    _uiState.value = ArticleUiState(isLoading = false, event = event)
                    checkIfSaved(eventId) // Check if the event is saved
                } else {
                    _uiState.value = ArticleUiState(isLoading = false, errorMessage = "Event not found.")
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState(isLoading = false, errorMessage = "Error: ${e.message}")
            }
        }
    }

    fun fetchRandomEvent() {
        viewModelScope.launch {
            try {
                _uiState.value = ArticleUiState(isLoading = true)
                val event = repository.getRandomEvent()
                if (event != null) {
                    _uiState.value = ArticleUiState(isLoading = false, event = event)
                    checkIfSaved(event.id) // Check if the random event is saved
                } else {
                    _uiState.value = ArticleUiState(isLoading = false, errorMessage = "No events found or error fetching random event.")
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState(isLoading = false, errorMessage = "Error: ${e.message}")
            }
        }
    }

    fun toggleSavePost(eventId: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value.isSaved
                if (currentState) {
                    savedPostRepository.unsavePost(eventId)
                } else {
                    savedPostRepository.savePost(eventId)
                }
                _uiState.value = _uiState.value.copy(isSaved = !currentState)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error saving post: ${e.message}"
                )
            }
        }
    }

    private suspend fun checkIfSaved(eventId: String) {
        val isSaved = savedPostRepository.isPostSaved(eventId)
        _uiState.value = _uiState.value.copy(isSaved = isSaved)
    }
}