package com.mad.susach.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.mad.susach.MainActivity  // Fix import
import com.mad.susach.viewmodel.AuthViewModel

class LoginActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if user is already logged in
        if (viewModel.getCurrentUser() != null) {
            startMainActivity()
            return
        }

        setContent {
            MaterialTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { startMainActivity() },
                    onNavigateToRegister = { /* TODO: Navigate to register */ }
                )
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
