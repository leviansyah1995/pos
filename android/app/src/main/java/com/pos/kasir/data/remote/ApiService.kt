package com.pos.kasir.data.remote

import com.pos.kasir.data.remote.dto.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @GET("menu")
    suspend fun getMenu(): MenuResponse

    @POST("menu")
    suspend fun createMenu(@Body body: Map<String, Any>): MenuItemDto

    @POST("orders")
    suspend fun createOrder(@Body req: CreateOrderRequest): OrderDto

    @GET("orders")
    suspend fun getOrders(@Query("status") status: String? = null): OrdersResponse

    @PATCH("orders/{id}/status")
    suspend fun updateStatus(@Path("id") id: String, @Body req: StatusRequest): OrderDto
}
