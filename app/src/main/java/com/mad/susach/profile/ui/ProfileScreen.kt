package com.mad.susach.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.susach.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onNavigateToSavedPosts: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isDetailedViewVisible) {
        DetailedProfileScreen(viewModel = viewModel)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDF6F0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile Image
            Image(
                painter = painterResource(id = R.drawable.default_avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username
            Text(
                text = viewModel.username ?: "User",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Profile Options
            ProfileOptionItem(
                text = "Thông tin cá nhân",
                onClick = { viewModel.showDetailedProfile() }
            )
            ProfileOptionItem(
                text = "Bài viết đã lưu",
                onClick = onNavigateToSavedPosts
            )
            ProfileOptionItem("Thành tích")
            ProfileOptionItem("Cài đặt")
            ProfileOptionItem(
                text = "Đăng xuất",
                onClick = onLogout,
                textColor = Color(0xFFFF6600)
            )
        }
    }
}

@Composable
private fun ProfileOptionItem(
    text: String,
    onClick: () -> Unit = {},
    textColor: Color = Color(0xFF1A1A1A)
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        color = Color.White,
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = textColor
            )
        }
    }
}
