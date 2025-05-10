package com.mad.susach

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SuSachApp : Application() {

    companion object {
        private lateinit var instance: SuSachApp
        
        fun getInstance(): SuSachApp = instance
        
        // Firebase instances
        val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
        val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize other libraries here
        setupTimber()
    }

    private fun setupTimber() {
        // TODO: Setup logging
    }
}
