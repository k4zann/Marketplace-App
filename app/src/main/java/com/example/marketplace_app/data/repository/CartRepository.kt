package com.example.marketplace_app.data.repository

import com.example.marketplace_app.data.local.CartItemDao
import com.example.marketplace_app.data.mappers.toEntity
import com.example.marketplace_app.data.mappers.toPresentation
import com.example.marketplace_app.data.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val cartDao: CartItemDao) {


    suspend fun getAllProductsFromCart(): List<Product> =
        withContext(Dispatchers.IO) {
            cartDao.getAllCartItems().map { it.toPresentation() }
        }

    suspend fun addProductToCart(product: Product) {
        withContext(Dispatchers.IO) {
            cartDao.insertCartItem(product.toEntity())

        }
    }

    suspend fun removeProductFromCart(product: Product) {
        withContext(Dispatchers.IO) {
            cartDao.deleteCartItem(product.toEntity())
        }
    }

    suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            cartDao.clearCart()
        }
    }
}