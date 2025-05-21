package com.mad.susach.auth.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
        viewModelScope.launch {
            try {
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                Log.d("LoginViewModel", "Attempting login for $email")
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Log.d("LoginViewModel", "Login success: ${'$'}{result.user?.uid}")
                Log.d("LoginViewModel", "Login successful, checking user document...")
                
                // Verify user document exists
                val userDoc = firestore.collection("users")
                    .document(result.user?.uid ?: "")
                    .get()
                    .await()
                
                Log.d("LoginViewModel", "User document exists: ${userDoc.exists()}")
                
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true, error = null)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed", e)
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = false, error = e.message ?: e.toString())
            }
        }
    }
}
