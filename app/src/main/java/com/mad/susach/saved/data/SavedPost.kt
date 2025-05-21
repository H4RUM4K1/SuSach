package com.mad.susach.saved.data

import com.google.firebase.Timestamp

data class SavedPost(
    val userId: String = "",
    val eventId: String = "",
    val savedAt: Timestamp = Timestamp.now()
)
