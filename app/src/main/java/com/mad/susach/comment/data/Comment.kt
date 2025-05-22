package com.mad.susach.comment.data

data class Comment(
    val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
