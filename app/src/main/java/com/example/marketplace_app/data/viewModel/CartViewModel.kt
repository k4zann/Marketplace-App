package com.example.marketplace_app.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace_app.data.models.CartEvent
import com.example.marketplace_app.data.models.CartState
import com.example.marketplace_app.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), CartState()
    )

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.AddToCart -> {
                viewModelScope.launch {
                    cartRepository.addProductToCart(event.product)
                }
            }
            is CartEvent.RemoveFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeProductFromCart(event.product)
                }
            }
            is CartEvent.LoadCart -> {
                viewModelScope.launch {
                    val cartItems = cartRepository.getAllProductsFromCart()
                    _state.value = CartState(cartItems = cartItems)
                }
            }
            is CartEvent.ClearCart -> {
                viewModelScope.launch {
                    cartRepository.clearCart()
                    _state.value = CartState(cartItems = emptyList())
                }
            }
        }
    }

}