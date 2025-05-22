package com.mad.susach.comment.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CommentRepository {
    private val db = FirebaseFirestore.getInstance()
    private val commentsCollection = db.collection("comments")

    fun getCommentsForEvent(eventId: String): Flow<List<Comment>> = callbackFlow {
        val subscription = commentsCollection
            .whereEqualTo("eventId", eventId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val comments = snapshot?.documents?.mapNotNull {
                    it.toObject(Comment::class.java)
                } ?: emptyList()
                trySend(comments)
            }

        awaitClose { subscription.remove() }
    }

    suspend fun addComment(comment: Comment): Comment {
        val documentRef = commentsCollection.document()
        val commentWithId = comment.copy(id = documentRef.id)
        documentRef.set(commentWithId).await()
        return commentWithId
    }
}
