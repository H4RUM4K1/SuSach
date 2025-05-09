package com.mad.susach.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation // Add this import
import androidx.compose.ui.text.input.TextFieldValue
import com.mad.susach.model.data.User
import com.mad.susach.viewmodel.AuthViewModel
import com.mad.susach.viewmodel.AuthViewModel.AuthState

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.register(
                    email,
                    password,
                    User(
                        email = email,
                        fullName = fullName,
                        dateOfBirth = dateOfBirth,
                        phoneNumber = phone
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        when (state) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Error -> Text((state as AuthState.Error).message)
            is AuthState.Success -> LaunchedEffect(Unit) { onRegisterSuccess() }
            else -> Unit
        }
    }
}
