package com.mad.susach.model.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.model.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun register(email: String, password: String, user: User): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                // 1. Create auth account
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user!!.uid

                // 2. Save user data to Firestore
                db.collection("users")
                    .document(userId)
                    .set(user.copy(id = userId))
                    .await()

                Result.success(authResult.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
