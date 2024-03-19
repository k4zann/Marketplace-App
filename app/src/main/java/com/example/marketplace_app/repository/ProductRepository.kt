package com.example.marketplace_app.repository

import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Из пятого урока посмотри на пример Repository, сделай похоже
class ProductRepository(private val productApi: ProductApi) {

    suspend fun getProducts(skip: Int, limit: Int): List<Product> =
        withContext(Dispatchers.IO) {
            productApi.getProducts(skip, limit).products
        }

    suspend fun getProduct(id: Long): Product =
        withContext(Dispatchers.IO) {
            productApi.getProduct(id)
        }

    suspend fun searchProducts(query: String): List<Product> =
        withContext(Dispatchers.IO) {
            productApi.searchProducts(query).products
        }

    suspend fun getCategories(): List<String> =
        withContext(Dispatchers.IO) {
            productApi.getCategories()
        }

    suspend fun getProductsByCategory(category: String): List<Product> =
        withContext(Dispatchers.IO) {
            productApi.getProductsByCategory(category).products
        }
}
