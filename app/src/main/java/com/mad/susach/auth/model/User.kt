package com.mad.susach.auth.model

data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
    val avatarUrl: String = "",
    val savedArticles: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
