package com.mad.susach.saved.data

import com.google.firebase.Timestamp

data class SavedPost(
    val eventId: String = "",
    val nameEvent: String = "",
    val userId: String = "",
    val savedAt: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
)
