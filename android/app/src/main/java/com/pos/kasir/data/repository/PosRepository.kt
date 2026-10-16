package com.pos.kasir.data.repository

import com.pos.kasir.data.local.AppDatabase
import com.pos.kasir.data.local.entity.MenuItemEntity
import com.pos.kasir.data.local.entity.OrderEntity
import com.pos.kasir.data.remote.ApiService
import com.pos.kasir.data.remote.dto.*
import kotlinx.coroutines.flow.Flow

class PosRepository(private val api: ApiService, private val db: AppDatabase) {
    fun menuFlow(): Flow<List<MenuItemEntity>> = db.menuDao().getAll()
    fun ordersFlow(): Flow<List<OrderEntity>> = db.orderDao().getAll()

    suspend fun refreshMenu() {
        val res = api.getMenu()
        db.menuDao().clear()
        db.menuDao().insertAll(res.items.map { MenuItemEntity(it.id, it.name, it.price, it.category, it.available) })
    }

    suspend fun createOrder(items: List<OrderItemRequest>): OrderDto {
        val order = api.createOrder(CreateOrderRequest(items))
        db.orderDao().insert(OrderEntity(order.id, order.total, order.status, "", order.createdAt))
        return order
    }

    suspend fun updateStatus(id: String, status: String): OrderDto {
        val order = api.updateStatus(id, StatusRequest(status))
        db.orderDao().updateStatus(id, status)
        return order
    }

    suspend fun loadOrders(status: String? = null): List<OrderDto> = api.getOrders(status).orders
}
