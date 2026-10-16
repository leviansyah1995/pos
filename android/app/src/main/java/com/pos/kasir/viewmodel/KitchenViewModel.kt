package com.pos.kasir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pos.kasir.ServiceLocator
import com.pos.kasir.data.remote.dto.OrderDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KitchenViewModel : ViewModel() {
    private val repo = ServiceLocator.repository()
    private val _orders = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> = _orders.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            runCatching {
                val pending = repo.loadOrders("pending")
                val preparing = repo.loadOrders("preparing")
                val ready = repo.loadOrders("ready")
                _orders.value = pending + preparing + ready
            }
        }
    }

    fun setStatus(id: String, status: String) {
        viewModelScope.launch {
            runCatching { repo.updateStatus(id, status) }
            refresh()
        }
    }
}
