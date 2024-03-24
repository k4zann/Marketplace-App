package com.example.marketplace_app.data.models

data class CartState(
    val isLoading: Boolean = false,
    val cartItems: List<Product> = emptyList(),
    val isSuccess: Boolean = false,
)