package com.pos.kasir.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pos.kasir.viewmodel.AuthState
import com.pos.kasir.viewmodel.AuthViewModel

@Composable
fun LoginScreen(nav: NavController) {
    val vm: AuthViewModel = viewModel()
    val state by vm.state.collectAsState()
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is AuthState.Success) nav.navigate("cashier") { popUpTo("login") { inclusive = true } }
    }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("POS Kasir", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("Username") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.login(user, pass) }) { Text("Login") }
        if (state is AuthState.Error) Text((state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
        if (state is AuthState.Loading) CircularProgressIndicator()
    }
}
