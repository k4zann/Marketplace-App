package com.example.marketplace_app.data

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("title") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val productPrice: Int,
    @SerializedName("thumbnail") val poster: String
)