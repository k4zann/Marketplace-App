package com.example.marketplace_app

import android.app.Application
import android.util.Log
import com.example.marketplace_app.data.local.CartDatabase
import com.example.marketplace_app.data.repository.CartRepository
import com.example.marketplace_app.data.repository.ProductRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
