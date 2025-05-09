package com.mad.susach.main.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.mad.susach.main.auth.login.repository.LoginRepository

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.login(email, password)
                .onSuccess { _authState.value = AuthState.Success }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Đăng nhập thất bại") }
        }
    }

    sealed class AuthState {
        object Initial : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
