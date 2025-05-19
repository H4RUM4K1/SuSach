package com.mad.susach.article.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.event.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArticleUiState(
    val isLoading: Boolean = true,
    val event: Event? = null,
    val errorMessage: String? = null
)

class ArticleViewModel(private val repository: EventRepository = EventRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

    fun fetchEvent(eventId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ArticleUiState(isLoading = true)
                val event = repository.getEventById(eventId)
                if (event != null) {
                    _uiState.value = ArticleUiState(isLoading = false, event = event)
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
                } else {
                    _uiState.value = ArticleUiState(isLoading = false, errorMessage = "No events found or error fetching random event.")
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState(isLoading = false, errorMessage = "Error: ${e.message}")
            }
        }
    }
}