package com.mad.susach.profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.susach.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Thông tin cá nhân",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text("Họ tên: ${uiState.user.fullName}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Email: ${uiState.user.email}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ngày sinh: ${uiState.user.dateOfBirth}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Số điện thoại: ${uiState.user.phoneNumber}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Địa chỉ: ${uiState.user.address}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng xuất")
        }
    }
}
