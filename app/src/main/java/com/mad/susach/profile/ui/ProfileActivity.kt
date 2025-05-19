package com.mad.susach.profile.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.mad.susach.auth.login.ui.LoginActivity

class ProfileActivity : ComponentActivity() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
            )
        }
    }
}
