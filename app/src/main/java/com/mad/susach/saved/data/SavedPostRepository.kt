package com.mad.susach.saved.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.event.data.Event
import kotlinx.coroutines.tasks.await

class SavedPostRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun savePost(eventId: String) {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        firestore.collection("users")
            .document(userId)
            .collection("saved_events")
            .document(eventId)
            .set(hashMapOf(
                "eventId" to eventId,
                "savedAt" to System.currentTimeMillis()
            )).await()
    }

    suspend fun unsavePost(eventId: String) {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        firestore.collection("users")
            .document(userId)
            .collection("saved_events")
            .document(eventId)
            .delete()
            .await()
    }

    suspend fun isPostSaved(eventId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        return firestore.collection("users")
            .document(userId)
            .collection("saved_events")
            .document(eventId)
            .get()
            .await()
            .exists()
    }

    suspend fun getSavedPosts(): List<Event> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        
        // Get all saved event IDs
        val savedEventIds = firestore.collection("users")
            .document(userId)
            .collection("saved_events")
            .get()
            .await()
            .documents
            .map { it.id }

        // Fetch full event data for each saved ID
        return savedEventIds.mapNotNull { eventId ->
            firestore.collection("events")
                .document(eventId)
                .get()
                .await()
                .toObject(Event::class.java)
                ?.copy(id = eventId)
        }
    }
}
