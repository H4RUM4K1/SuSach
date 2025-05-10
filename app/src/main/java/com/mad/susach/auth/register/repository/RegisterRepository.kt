package com.mad.susach.auth.register.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.auth.register.data.User
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun register(email: String, password: String, user: User): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user!!.uid
                
                db.collection("users")
                    .document(userId)
                    .set(user.copy(id = userId))
                    .await()

                Result.success(authResult.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
