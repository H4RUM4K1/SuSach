package com.mad.susach.timeline.ui.timelineview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.timeline.data.Era
import com.mad.susach.timeline.data.EraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimelineViewModel(
    private val eraRepository: EraRepository,
    eraId: String
) : ViewModel() {

    // Hiển thị danh sách sự kiện
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    // Era hiện tại
    private val _selectedEra = MutableStateFlow<Era?>(null)
    val selectedEra: StateFlow<Era?> = _selectedEra.asStateFlow()

    // LoadingLoading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ErrorError
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadEventsForEra(eraId)
    }

    // Load tất cả event cho era này
    fun loadEventsForEra(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = eraRepository.getEventsForEra(id)
                _events.value = result
            } catch (e: Exception) {
                _error.value = "Tải sự kiện thất bại: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Load chi tiết era
    fun loadEraDetails(id: String) {
        viewModelScope.launch {
            try {
                _selectedEra.value = eraRepository.getEraById(id)
            } catch (e: Exception) {
                _error.value = "Failed to load era details: ${e.message}"
            }
        }
    }

    companion object {
        fun provideFactory(
            eraRepository: EraRepository,
            eraId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
                    return TimelineViewModel(eraRepository, eraId) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
