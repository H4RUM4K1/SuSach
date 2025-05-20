package com.mad.susach.auth.model

data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val dateOfBirth: String = "",
    val phoneNumber: String = "",
    val address: String = ""
)
