package com.pos.kasir.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val total: Int,
    val status: String,
    val itemsJson: String,
    val createdAt: String
)
