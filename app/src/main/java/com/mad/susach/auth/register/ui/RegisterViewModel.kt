package com.mad.susach.auth.register.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        // TODO: Implement Firebase Auth registration
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isSuccess = true
        )
    }
}
