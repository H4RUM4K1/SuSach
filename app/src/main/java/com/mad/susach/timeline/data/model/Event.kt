package com.mad.susach.timeline.data.model

data class Event(
    val id: String,
    val eraId: String,
    val name: String,
    val description: String,
    val year: Int,
    val imageUrl: String? = null,
    val articleId: String // To link to a detailed article
)
