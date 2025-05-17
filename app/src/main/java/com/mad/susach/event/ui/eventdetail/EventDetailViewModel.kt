package com.mad.susach.event.ui.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.model.Event // Updated import
import com.mad.susach.event.data.repository.EventRepository // Will be created
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventDetailViewModel(
    // TODO: Replace with DI-injected repository
    private val eventRepository: EventRepository = EventRepository() 
) : ViewModel() {

    private val _eventDetail = MutableStateFlow<Event?>(null)
    val eventDetail: StateFlow<Event?> = _eventDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadEventDetail(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _eventDetail.value = eventRepository.getEventById(eventId) 
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
            _isLoading.value = false
        }
    }
}
