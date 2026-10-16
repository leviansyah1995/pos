package com.pos.kasir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pos.kasir.ServiceLocator
import com.pos.kasir.data.remote.dto.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val res = ServiceLocator.api().login(LoginRequest(username, password))
                ServiceLocator.token = res.token
                _state.value = AuthState.Success(res.user.username, res.user.role)
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Login gagal")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val username: String, val role: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
