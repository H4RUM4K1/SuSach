package com.mad.susach.auth.register.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.auth.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private fun validateRegister(email: String, password: String, user: User): String? {
        if (email.isEmpty()) return "Email không được để trống"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ"
        }
        if (password.isEmpty()) return "Mật khẩu không được để trống"
        if (password.length < 6) return "Mật khẩu phải có ít nhất 6 ký tự"
        if (user.fullName.isEmpty()) return "Họ tên không được để trống"
        if (user.dateOfBirth.isEmpty()) return "Ngày sinh không được để trống"
        if (user.phoneNumber.isEmpty()) return "Số điện thoại không được để trống"
        if (!user.phoneNumber.matches(Regex("^[0-9]{10}$"))) {
            return "Số điện thoại không hợp lệ" 
        }
        return null
    }

    fun register(email: String, password: String, user: User) {
        val errorMessage = validateRegister(email, password, user)
        if (errorMessage != null) {
            _uiState.value = RegisterUiState(error = errorMessage)
            return
        }

        _uiState.value = RegisterUiState(isLoading = true)
        
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid ?: return@addOnSuccessListener
                firestore.collection("users")
                    .document(userId)
                    .set(user.copy(id = userId))
                    .addOnSuccessListener {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                    .addOnFailureListener { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
    }
}
