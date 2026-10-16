package com.pos.kasir.data.remote.dto

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val user: UserDto)
data class UserDto(val id: String, val username: String, val role: String)

data class MenuItemDto(val id: String, val name: String, val price: Int, val category: String, val available: Boolean)
data class MenuResponse(val items: List<MenuItemDto>)

data class OrderItemRequest(val menuId: String, val qty: Int)
data class CreateOrderRequest(val items: List<OrderItemRequest>)
data class OrderItemDto(val menuId: String, val name: String, val price: Int, val qty: Int, val subtotal: Int? = null)
data class OrderDto(val id: String, val items: List<OrderItemDto>, val total: Int, val status: String, val createdAt: String)
data class OrdersResponse(val orders: List<OrderDto>)
data class StatusRequest(val status: String)
