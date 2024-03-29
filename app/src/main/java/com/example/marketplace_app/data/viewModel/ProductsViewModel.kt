package com.example.marketplace_app.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    private val _searchResults = MutableLiveData<List<Product>>()
    val searchResults: LiveData<List<Product>>
        get() = _searchResults

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData


    fun loadProducts() {
        viewModelScope.launch {
            try {
                val products = productRepository.getProducts(0, 20)
                _products.postValue(products)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to load products", e)
            }
        }
    }

    fun loadMoreProducts() {
        _isLoadingLiveData.value = true
        viewModelScope.launch {
            try {
                val products = productRepository.getProducts(_products.value?.size ?: 0, 20)
                _products.postValue(_products.value?.plus(products))
            } catch (e: Exception) {
                Log.d("ProductsViewModel", "Failed to load more products", e)
            } finally {
                _isLoadingLiveData.value = false
            }
        }
    }

    fun loadProduct(id: Long) {
        // замени на viewModelScope
        viewModelScope.launch {
            try {
                val product = productRepository.getProduct(id)
                _product.postValue(product)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to load product", e)
            }
        }
    }


    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = listOf("All") + productRepository.getCategories().map {
                    it.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase() else char.toString()
                    }
                }
                _categories.postValue(categories)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to load categories", e)
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                val products = productRepository.searchProducts(query)
                _products.postValue(products)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to search products", e)
            }
        }
    }

    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            try {
                val products = if (category == "All") {
                    productRepository.getProducts(0, 20)
                } else {
                    productRepository.getProductsByCategory(category)
                }
                _products.postValue(products)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to load products by category", e)
            }
        }
    }




}