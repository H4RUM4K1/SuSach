package com.mad.susach.event.data.model

data class Event(
    val id: String = "",
    val eraId: String = "",
    val name: String = "",
    val description: String = "",
    val imageURL: String = "",
    val startDate: Int = 0,
    val endDate: Int = 0,
    val imageContent: String = "",
    val summary: String = "",
    var contents: String = ""
)
