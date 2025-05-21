package com.mad.susach.auth.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.model.User  // Ensure using the correct User model
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.mad.susach.SuSachApp

class UserRepository {
    private val auth = SuSachApp.auth
    private val firestore = SuSachApp.firestore

    fun getCurrentUserId(): String? {
        val user = auth.currentUser
        Log.d("UserRepository", "Current user ID: ${user?.uid}")
        return user?.uid
    }

    suspend fun login(email: String, password: String): Result<Unit> = 
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun register(email: String, password: String, user: User): Result<User> {
        return try {
            // Create auth account
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Failed to create user")
            
            // Create user document in Firestore
            val userDoc = user.copy(id = userId, email = email)
            firestore.collection("users")
                .document(userId)
                .set(userDoc)
                .await()
            
            Log.d("UserRepository", "User created successfully with ID: $userId")
            Result.success(userDoc)
        } catch (e: Exception) {
            Log.e("UserRepository", "Registration failed", e)
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(user: User): Result<Unit> = 
        try {
            firestore.collection("users")
                .document(user.id)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getUserProfile(): Result<User> {
        return try {
            val currentUser = auth.currentUser
            Log.d("UserRepository", "Getting profile for user: ${currentUser?.uid}")
            
            if (currentUser == null) {
                Log.e("UserRepository", "No authenticated user found")
                return Result.failure(Exception("Vui lòng đăng nhập lại"))
            }

            val documentRef = firestore.collection("users").document(currentUser.uid)
            val document = documentRef.get().await()

            if (!document.exists() || document.data.isNullOrEmpty()) {
                val defaultUsername = currentUser.email?.substringBefore("@") ?: "User${currentUser.uid.take(5)}"
                val newUser = User(
                    id = currentUser.uid,
                    email = currentUser.email ?: "",
                    username = defaultUsername,
                    displayName = defaultUsername,
                    fullName = "",
                    phoneNumber = "",
                    address = "",
                    dateOfBirth = "",
                    avatarUrl = ""
                )
                
                updateUserProfile(newUser).getOrThrow()
                return Result.success(newUser)
            }

            val userData = document.toObject(User::class.java)
            if (userData == null) {
                Log.e("UserRepository", "Failed to parse user data: ${document.data}")
                return Result.failure(Exception("Lỗi định dạng dữ liệu người dùng"))
            }

            val finalUserData = userData.copy(id = currentUser.uid)
            Log.d("UserRepository", "Successfully retrieved user data: $finalUserData")
            
            Result.success(finalUserData)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting profile", e)
            Result.failure(e)
        }
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: ""
        )
    }

    fun logout() {
        auth.signOut()
    }
}
