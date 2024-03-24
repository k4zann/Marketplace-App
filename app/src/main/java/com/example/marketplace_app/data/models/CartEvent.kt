package com.example.marketplace_app.data.models

sealed interface CartEvent {
    data class AddToCart(val product: Product) : CartEvent
    data class RemoveFromCart(val product: Product) : CartEvent
    object LoadCart : CartEvent
    object ClearCart : CartEvent
}