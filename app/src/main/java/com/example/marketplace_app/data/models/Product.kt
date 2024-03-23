package com.example.marketplace_app.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable as Serializable

@Serializable
data class Product(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val productPrice: Int,
    @SerializedName("thumbnail") val poster: String,
    @SerializedName("category") val category: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("stock") val stock: Int,
    @SerializedName("brand") val brand: String,
    @SerializedName("images") val images: List<String>?
)
@Serializable
data class ProductList(
    @SerializedName("products") val products: List<Product>,
    @SerializedName("total") val total: Int
)

