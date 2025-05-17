package com.mad.susach.timeline.data.model

data class Era(
    val id: String,
    val name: String,
    val description: String? = null,
    val startYear: Int,
    val endYear: Int
)
