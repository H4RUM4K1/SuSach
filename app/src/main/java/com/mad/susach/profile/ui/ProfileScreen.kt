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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

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
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                shadowElevation = 8.dp,
                modifier = Modifier.size(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Username
            Text(
                text = viewModel.username ?: "User",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Profile Options
            ProfileOptionItem(
                text = "Thông tin cá nhân",
                onClick = { viewModel.showDetailedProfile() },
                icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFFF6600)) }
            )
            ProfileOptionItem(
                text = "Bài viết đã lưu",
                onClick = onNavigateToSavedPosts,
                icon = { Icon(Icons.Default.Bookmark, contentDescription = null, tint = Color(0xFFFF6600)) }
            )
            ProfileOptionItem(
                text = "Thành tích",
                icon = { Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFF6600)) }
            )
            ProfileOptionItem(
                text = "Cài đặt",
                icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFFFF6600)) }
            )
            ProfileOptionItem(
                text = "Đăng xuất",
                onClick = onLogout,
                textColor = Color(0xFFFF6600),
                icon = { Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFFF6600)) }
            )
        }
    }
}

@Composable
private fun ProfileOptionItem(
    text: String,
    onClick: () -> Unit = {},
    textColor: Color = Color(0xFF1A1A1A),
    icon: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 7.dp)
            .clickable(onClick = onClick),
        color = Color.White,
        shadowElevation = 3.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(14.dp))
            }
            Text(
                text = text,
                fontSize = 17.sp,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
