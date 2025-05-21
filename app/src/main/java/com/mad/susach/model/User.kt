package com.mad.susach.model

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("id") val id: String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("username") val username: String = "",
    @PropertyName("displayName") val displayName: String = "",
    @PropertyName("fullName") val fullName: String = "",
    @PropertyName("phoneNumber") val phoneNumber: String = "",
    @PropertyName("address") val address: String = "",
    @PropertyName("dateOfBirth") val dateOfBirth: String = "",
    @PropertyName("avatarUrl") val avatarUrl: String = ""
) {
    // Required empty constructor for Firestore
    constructor() : this("", "", "", "", "", "", "", "", "")
}
