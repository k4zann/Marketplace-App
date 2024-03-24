package com.example.marketplace_app.ui

import android.app.Application
import android.util.Log
import com.example.marketplace_app.data.api.ProductApi
import com.example.marketplace_app.data.local.CartDatabase
import com.example.marketplace_app.data.repository.CartRepository
import com.example.marketplace_app.data.repository.ProductRepository

class MainApplication: Application() {

    val repository by lazy {
        ProductRepository(
            ProductApi.INSTANCE,
        )
    }
    val cartRepository by lazy {
        CartRepository(
            cartDatabase.cartItemDao()
        )
    }
    private lateinit var cartDatabase: CartDatabase

    override fun onCreate() {
        super.onCreate()
        cartDatabase = CartDatabase.create(applicationContext)
        Log.d("MainApplication", "onCreate: CartDatabase created $cartDatabase")
    }
}