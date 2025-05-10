package com.mad.susach.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private fun validateLogin(email: String, password: String): String? {
        if (email.isEmpty()) return "Email không được để trống"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ"
        }
        if (password.isEmpty()) return "Mật khẩu không được để trống"
        if (password.length < 6) return "Mật khẩu phải có ít nhất 6 ký tự"
        return null
    }

    fun login(email: String, password: String) {
        val errorMessage = validateLogin(email, password)
        if (errorMessage != null) {
            _uiState.value = LoginUiState(error = errorMessage)
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)
        // TODO: Implement Firebase Auth
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isSuccess = true
        )
    }
}
