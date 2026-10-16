package com.pos.kasir.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class MenuItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Int,
    val category: String,
    val available: Boolean
)
