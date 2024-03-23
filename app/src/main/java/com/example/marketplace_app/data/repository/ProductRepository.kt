package com.example.marketplace_app.data.repository

import android.util.Log
import com.example.marketplace_app.data.api.ProductApi
import com.example.marketplace_app.data.local.CartItemDao
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.data.mappers.toPresentation
import com.example.marketplace_app.data.mappers.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val productApi: ProductApi, private val cartDao: CartItemDao?) {

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

    suspend fun getAllProductsFromCart(): List<Product> =
        withContext(Dispatchers.IO) {
            cartDao?.getAllCartItems()!!.map { it.toPresentation() }
        }

    suspend fun addProductToCart(product: Product) {
        withContext(Dispatchers.IO) {
            cartDao?.insertCartItem(product.toEntity())

        }
    }

    suspend fun removeProductFromCart(product: Product) {
        withContext(Dispatchers.IO) {
            cartDao?.deleteCartItem(product.toEntity())
        }
    }
}
