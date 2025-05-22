package com.mad.susach.comment.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class CommentRepository {
    private val db = FirebaseFirestore.getInstance()
    private val commentsCollection = db.collection("comments")

    suspend fun getCommentsForEvent(eventId: String): List<Comment> {
        return try {
            commentsCollection
                .whereEqualTo("eventId", eventId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(Comment::class.java)
        } catch (e: Exception) {
            // Handle index error
            if (e.message?.contains("FAILED_PRECONDITION") == true) {
                // Return empty list for now
                emptyList()
            } else {
                throw e
            }
        }
    }

    suspend fun addComment(comment: Comment): Comment {
        val documentRef = commentsCollection.document()
        val commentWithId = comment.copy(id = documentRef.id)
        documentRef.set(commentWithId).await()
        return commentWithId
    }
}
