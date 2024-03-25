package com.example.marketplace_app.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marketplace_app.data.repository.CartRepository
import com.example.marketplace_app.utils.repository.DataRepository

class CartViewModelFactory(private val cartRepository: CartRepository, private val dataRepository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository, dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}