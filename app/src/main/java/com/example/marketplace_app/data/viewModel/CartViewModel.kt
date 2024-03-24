package com.example.marketplace_app.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace_app.data.models.CartEvent
import com.example.marketplace_app.data.models.CartState
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
@HiltViewModel
class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), CartState()
    )
//    private val _addedProductsLiveData = MutableLiveData<List<Product>>()
//    val addedProductLiveData: LiveData<List<Product>>
//        get() = _addedProductsLiveData
//
//    fun loadAddedProducts() {
//        viewModelScope.launch {
//            val result = cartRepository.getAllProductsFromCart()
//            _addedProductsLiveData.value = result
//        }
//    }
//
//    fun addToCard(product: Product) {
//        viewModelScope.launch {
//            cartRepository.addProductToCart(product)
//        }
//    }

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
                    Log.d("CartViewModel", "Cart Items: $cartItems")
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