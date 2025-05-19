package com.mad.susach.timeline.ui.timelineview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.article.data.model.Article
import com.mad.susach.event.data.model.Event
import com.mad.susach.timeline.data.model.Era
import com.mad.susach.timeline.data.repository.EraRepository
import com.mad.susach.timeline.data.repository.TimelineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimelineViewModel(
    private val repository: TimelineRepository = TimelineRepository(), // Basic injection
    private val eraRepository: EraRepository = EraRepository(),
    val eraId: String = ""
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    private val _selectedEra = MutableStateFlow<Era?>(null)
    val selectedEra: StateFlow<Era?> = _selectedEra.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        if (eraId.isNotEmpty()) {
            loadEventsForEra(eraId)
        } else {
            _error.value = "Era ID not provided."
        }
    }

    fun loadEventsForEra(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                android.util.Log.d("TimelineViewModel", "Fetching events for eraId=$id")
                val result = repository.getEventsForEra(id)
                android.util.Log.d("TimelineViewModel", "Fetched events: count=${result.size}, data=$result")
                _events.value = result
            } catch (e: Exception) {
                android.util.Log.e("TimelineViewModel", "Failed to fetch events for eraId=$id: ${e.message}", e)
                _error.value = "Failed to load events: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
        // Optionally load article directly or navigate and then load
        // Use event.summary or event.content if you want, but Event does not have articleId
        // Remove loadArticleForEvent(event.articleId)
    }
    
    private fun loadArticleForEvent(articleId: String) {
        viewModelScope.launch {
            // Consider adding loading/error states for article loading specifically
            try {
                _selectedArticle.value = repository.getArticleById(articleId)
            } catch (e: Exception) {
                // Handle article loading error
                 _error.value = "Failed to load article: ${e.message}"
            }
        }
    }

    fun loadEraDetails(id: String) {
        viewModelScope.launch {
            try {
                _selectedEra.value = eraRepository.getEraById(id)
            } catch (e: Exception) {
                _error.value = "Failed to load era details: ${e.message}"
            }
        }
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
        _selectedArticle.value = null
    }
}
