package com.pos.kasir.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pos.kasir.data.local.dao.MenuDao
import com.pos.kasir.data.local.dao.OrderDao
import com.pos.kasir.data.local.entity.MenuItemEntity
import com.pos.kasir.data.local.entity.OrderEntity

@Database(entities = [MenuItemEntity::class, OrderEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(context, AppDatabase::class.java, "pos.db").build().also { INSTANCE = it }
            }
    }
}
