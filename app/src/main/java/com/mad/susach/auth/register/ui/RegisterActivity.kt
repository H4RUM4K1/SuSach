package com.mad.susach.auth.register.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.mad.susach.navigation.MainActivity
import com.mad.susach.auth.register.viewmodel.RegisterViewModel

class RegisterActivity : ComponentActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterContent(
                onRegisterSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity() // Đóng tất cả activities trước đó
                },
                onLoginClick = { finish() },
                viewModel = viewModel
            )
        }
    }
}
