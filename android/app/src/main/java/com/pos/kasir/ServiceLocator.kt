package com.pos.kasir

import android.content.Context
import com.pos.kasir.data.local.AppDatabase
import com.pos.kasir.data.local.AppDatabase as DB
import com.pos.kasir.data.repository.PosRepository
import com.pos.kasir.data.remote.RetrofitClient

object ServiceLocator {
    var token: String? = null
    lateinit var appContext: Context
    lateinit var db: AppDatabase
    fun api() = RetrofitClient.create { token }
    fun repository() = PosRepository(api(), db)
}
