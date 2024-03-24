package com.example.marketplace_app.data.models

data class CartState(
    val isLoading: Boolean = false,
    var cartItems: List<Product> = emptyList(),
    val isSuccess: Boolean = false,
)