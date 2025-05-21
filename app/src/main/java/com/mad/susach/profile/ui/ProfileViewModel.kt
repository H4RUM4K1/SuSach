package com.mad.susach.profile.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mad.susach.auth.repository.UserRepository
import com.mad.susach.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: UserData? = null,
    val isDetailedViewVisible: Boolean = false
)

data class UserData(
    val username: String = "",
    val email: String = "",
    val avatar: String? = null,
    val phoneNumber: String = "",
    val address: String = "",
    val dateOfBirth: String = ""
)

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    val username: String?
        get() = _uiState.value.userData?.username

    init {
        Log.d("ProfileViewModel", "Initializing with auth state: ${FirebaseAuth.getInstance().currentUser != null}")
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val auth = FirebaseAuth.getInstance()
                // Debug auth state
                Log.d("ProfileViewModel", "Current auth state - isSignedIn: ${auth.currentUser != null}")
                Log.d("ProfileViewModel", "Current auth user: ${auth.currentUser?.uid}")
                
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val userId = userRepository.getCurrentUserId()
                Log.d("ProfileViewModel", "Repository returned user ID: $userId")
                
                if (userId == null) {
                    Log.e("ProfileViewModel", "No user ID found")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Vui lòng đăng nhập lại"
                    )
                    return@launch
                }
                
                val result = userRepository.getUserProfile()
                result.fold(
                    onSuccess = { user ->
                        Log.d("ProfileViewModel", "Successfully loaded user profile: $user")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userData = UserData(
                                username = user.fullName,
                                email = user.email,
                                phoneNumber = user.phoneNumber,
                                address = user.address,
                                dateOfBirth = user.dateOfBirth
                            ),
                            error = null
                        )
                    },
                    onFailure = { e ->
                        Log.e("ProfileViewModel", "Failed to load user profile", e)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Không thể tải thông tin người dùng"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Unexpected error loading profile", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Lỗi không mong muốn: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        userRepository.logout()
    }

    fun showDetailedProfile() {
        _uiState.value = _uiState.value.copy(isDetailedViewVisible = true)
    }

    fun navigateBack() {
        _uiState.value = _uiState.value.copy(isDetailedViewVisible = false)
    }

    fun refresh() {
        loadUserProfile()
    }
}
