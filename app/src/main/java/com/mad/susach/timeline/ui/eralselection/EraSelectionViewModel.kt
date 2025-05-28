package com.mad.susach.timeline.ui.eralselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.timeline.data.Era
import com.mad.susach.timeline.data.EraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EraSelectionViewModel(
    private val eraRepository: EraRepository = EraRepository()
) : ViewModel() {

    // danh sách era
    private val _eras = MutableStateFlow<List<Era>>(emptyList())
    val eras: StateFlow<List<Era>> = _eras.asStateFlow()

    // Loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadEras()
    }

    // Lấy danh sách era từ Firestore
    private fun loadEras() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val eras = eraRepository.getEras()
                _eras.value = eras
            } catch (e: Exception) {
                _error.value = "Lỗi tải thời kỳ: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
