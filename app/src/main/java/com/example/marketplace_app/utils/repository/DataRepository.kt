package com.example.marketplace_app.utils.repository

import android.content.Context
import com.example.marketplace_app.data.local.CartItem
import com.example.marketplace_app.data.local.CartItemDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val cartDao: CartItemDao,
    private val context: Context
) {


    suspend fun fetchDataAndSaveInCache() {
        val data = cartDao.getAllCartItems()
        saveDataToCache(data)
    }

    private fun saveDataToCache(data: List<CartItem>) {
        val sharedPreferences = context.getSharedPreferences("cart_cache", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("cart_data", Gson().toJson(data)) // Using Gson for serialization
        editor.apply()
    }

    fun retrieveDataFromCache(): List<CartItem> {
        val sharedPreferences = context.getSharedPreferences("cart_cache", Context.MODE_PRIVATE)
        val jsonData = sharedPreferences.getString("cart_data", null)
        return if (jsonData != null) {
            Gson().fromJson(jsonData, object : TypeToken<List<CartItem>>() {}.type)
        } else {
            emptyList()
        }
    }
}