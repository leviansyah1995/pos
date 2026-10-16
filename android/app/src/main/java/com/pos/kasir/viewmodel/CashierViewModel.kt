package com.pos.kasir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pos.kasir.ServiceLocator
import com.pos.kasir.data.local.entity.MenuItemEntity
import com.pos.kasir.data.remote.dto.OrderDto
import com.pos.kasir.data.remote.dto.OrderItemRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartLine(val menu: MenuItemEntity, val qty: Int)

class CashierViewModel : ViewModel() {
    private val repo = ServiceLocator.repository()
    private val _menu = MutableStateFlow<List<MenuItemEntity>>(emptyList())
    val menu: StateFlow<List<MenuItemEntity>> = _menu.asStateFlow()

    private val _cart = MutableStateFlow<List<CartLine>>(emptyList())
    val cart: StateFlow<List<CartLine>> = _cart.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> = _orders.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            repo.menuFlow().collect { _menu.value = it }
        }
        viewModelScope.launch { runCatching { repo.refreshMenu() } }
        refreshOrders()
    }

    fun addToCart(m: MenuItemEntity) {
        val cur = _cart.value.toMutableList()
        val idx = cur.indexOfFirst { it.menu.id == m.id }
        if (idx >= 0) cur[idx] = cur[idx].copy(qty = cur[idx].qty + 1) else cur.add(CartLine(m, 1))
        _cart.value = cur
    }

    fun changeQty(id: String, delta: Int) {
        _cart.value = _cart.value.mapNotNull { line ->
            if (line.menu.id != id) line
            else {
                val q = line.qty + delta
                if (q <= 0) null else line.copy(qty = q)
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            try {
                val items = _cart.value.map { OrderItemRequest(it.menu.id, it.qty) }
                repo.createOrder(items)
                _cart.value = emptyList()
                _message.value = "Order dibuat"
                refreshOrders()
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun refreshOrders() {
        viewModelScope.launch {
            runCatching { _orders.value = repo.loadOrders() }
        }
    }

    fun completeOrder(id: String) {
        viewModelScope.launch {
            runCatching { repo.updateStatus(id, "completed") }
            refreshOrders()
        }
    }

    fun clearMessage() { _message.value = null }
}
