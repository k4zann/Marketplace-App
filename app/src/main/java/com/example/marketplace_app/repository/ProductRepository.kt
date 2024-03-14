package com.example.marketplace_app.repository

import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status

class ProductRepository(private val productApi: ProductApi) {

    fun getProducts(skip: Int, limit: Int): List<Product> {
        val response = productApi.getProducts(skip, limit).execute()
        if (response.isSuccessful) {
            return response.body()?.products ?: emptyList()
        } else {
            throw ApiException(Status(500, "Failed to fetch products: ${response.errorBody()}"))
        }
    }

    fun getProduct(id: Long): Product {
        val response = productApi.getProduct(id).execute()
        if (response.isSuccessful) {
            return response.body() ?: throw ApiException(Status(404, "Product not found"))
        } else {
            throw ApiException(Status(500, "Failed to fetch product: ${response.errorBody()}"))
        }
    }

    fun searchProducts(query: String): List<Product> {
        val response = productApi.searchProducts(query).execute()
        if (response.isSuccessful) {
            return response.body()?.products ?: emptyList()
        } else {
            throw ApiException(Status(500, "Failed to fetch products: ${response.errorBody()}"))
        }
    }

    fun getCategories(): List<String> {
        val response = productApi.getCategories().execute()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw ApiException(Status(500, "Failed to fetch products: ${response.errorBody()}"))
        }
    }

    fun getProductsByCategory(category: String): List<Product> {
        val response = productApi.getProductsByCategory(category).execute()
        if (response.isSuccessful) {
            return response.body()?.products ?: emptyList()
        } else {
            throw ApiException(Status(500, "Failed to fetch products: ${response.errorBody()}"))
        }
    }
}
