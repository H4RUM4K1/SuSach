package com.mad.susach.auth.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.mad.susach.main.MainActivity
import com.mad.susach.auth.login.viewmodel.LoginViewModel
import com.mad.susach.auth.login.viewmodel.LoginViewModelFactory

class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = { /* TODO */ },
                    onLoginSuccess = { 
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
