package com.mad.susach.timeline.ui.eralselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.timeline.data.model.Era
import com.mad.susach.timeline.data.repository.TimelineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EraSelectionViewModel(
    private val repository: TimelineRepository = TimelineRepository() // Basic injection for now
) : ViewModel() {

    private val _eras = MutableStateFlow<List<Era>>(emptyList())
    val eras: StateFlow<List<Era>> = _eras.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadEras()
    }

    fun loadEras() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // In a real app, repository.getEras() would be a suspend function
                // and might involve network calls or database queries.
                _eras.value = repository.getEras()
            } catch (e: Exception) {
                _error.value = "Failed to load eras: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
