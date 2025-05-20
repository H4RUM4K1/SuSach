package com.mad.susach.profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val userData = state.userData

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin cá nhân") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                userData?.let {
                    ProfileInfoCard(label = "Tên người dùng", value = it.username)
                    ProfileInfoCard(label = "Email", value = it.email)
                    ProfileInfoCard(label = "Số điện thoại", value = it.phoneNumber)
                    ProfileInfoCard(label = "Địa chỉ", value = it.address)
                    ProfileInfoCard(label = "Ngày sinh", value = it.dateOfBirth)
                }
            }
        }

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = { /* Dismiss error */ },
                title = { Text("Lỗi") },
                text = { Text(state.error!!) },
                confirmButton = {
                    TextButton(onClick = { /* Dismiss error */ }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileInfoCard(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (value.isBlank()) "Chưa cập nhật" else value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
