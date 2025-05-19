package com.mad.susach.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.model.Event
import com.mad.susach.event.data.repository.EventRepository
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
}