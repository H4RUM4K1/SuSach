package com.mad.susach.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.auth.repository.UserRepository
import com.mad.susach.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    val username: String?
        get() = _uiState.value.userData?.username

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val currentUser = userRepository.getCurrentUser()
                    ?: throw Exception("Bạn chưa đăng nhập")
                    
                val result = userRepository.getUserProfile(currentUser.id)
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null,
                            userData = UserData(
                                username = user.fullName,
                                email = user.email,
                                phoneNumber = user.phoneNumber,
                                address = user.address,
                                dateOfBirth = user.dateOfBirth
                            )
                        )
                    },
                    onFailure = { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Không thể tải thông tin người dùng"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi"
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
}
