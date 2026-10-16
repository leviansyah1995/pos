package com.pos.kasir

import android.app.Application
import com.pos.kasir.data.local.AppDatabase

class PosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.appContext = this
        ServiceLocator.db = AppDatabase.get(this)
    }
}
