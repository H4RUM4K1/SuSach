package com.mad.susach.profile.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.auth.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User = User(),
    val error: String? = null
)

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        _uiState.value = _uiState.value.copy(isLoading = true)

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                document.toObject(User::class.java)?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user
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

    fun logout() {
        auth.signOut()
    }
}
