package com.mad.susach.timeline.data

data class Era(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val startYear: Int = 0,
    val endYear: Int = 0,
    val imageUrl: String = ""
)
