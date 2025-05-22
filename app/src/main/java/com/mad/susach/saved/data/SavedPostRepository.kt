package com.mad.susach.saved.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mad.susach.event.data.Event
import com.mad.susach.saved.ui.SortOption
import kotlinx.coroutines.tasks.await

class SavedPostRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val COLLECTION_NAME = "saved_post"

    suspend fun savePost(event: Event) {
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
            val savedPost = SavedPost(
                eventId = event.id,
                nameEvent = event.name,
                userId = userId
            )
            
            firestore.collection(COLLECTION_NAME)
                .add(savedPost)
                .await()
        } catch (e: Exception) {
            throw Exception("Failed to save post: ${e.message}")
        }
    }

    suspend fun unsavePost(eventId: String) {
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
            
            val query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("eventId", eventId)
                .get()
                .await()
                
            for (document in query.documents) {
                document.reference.delete().await()
            }
        } catch (e: Exception) {
            throw Exception("Failed to unsave post: ${e.message}")
        }
    }

    suspend fun isPostSaved(eventId: String): Boolean {
        try {
            val userId = auth.currentUser?.uid ?: return false
            
            val query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("eventId", eventId)
                .get()
                .await()
                
            return !query.isEmpty
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun getSavedPosts(sortOption: SortOption = SortOption.SavedTime): List<Event> {
        try {
            val userId = auth.currentUser?.uid ?: return emptyList()
            
            // Tạo query dựa vào sortOption
            val baseQuery = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)

            // Áp dụng sắp xếp tương ứng
            val savedQuery = when (sortOption) {
                SortOption.Name -> baseQuery.orderBy("nameEvent", Query.Direction.ASCENDING)
                SortOption.SavedTime -> baseQuery.orderBy("savedAt", Query.Direction.DESCENDING)
            }.get().await()
                
            if (savedQuery.isEmpty) {
                return emptyList()
            }

            // Lấy thông tin chi tiết của từng event
            val eventsList = mutableListOf<Event>()
            
            for (savedDoc in savedQuery.documents) {
                val savedPost = savedDoc.toObject(SavedPost::class.java) ?: continue
                try {
                    val eventDoc = firestore.collection("events")
                        .document(savedPost.eventId)
                        .get()
                        .await()
                        
                    eventDoc.toObject(Event::class.java)?.let { event ->
                        eventsList.add(event.copy(id = eventDoc.id))
                    }
                } catch (e: Exception) {
                    continue
                }
            }
            
            return eventsList
        } catch (e: Exception) {
            throw Exception("Failed to get saved posts: ${e.message}")
        }
    }
}
