package com.mad.susach

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SuSachApp : Application() {

    companion object {
        // Firebase instances
        lateinit var auth: FirebaseAuth
            private set
        lateinit var firestore: FirebaseFirestore
            private set
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        
        // Initialize other libraries here
        setupTimber()
    }

    private fun setupTimber() {
        // TODO: Setup logging
    }
}
