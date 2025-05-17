package com.mad.susach.event.data.model

data class Event(
    val id: String,
    val eraId: String? = null,
    val name: String,
    val description: String,
    val year: Int,
    val date: String,
    val imageUrl: String? = null,
    val articleId: String

)
