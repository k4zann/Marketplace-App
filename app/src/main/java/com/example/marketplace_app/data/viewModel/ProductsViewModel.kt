package com.example.marketplace_app.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductsViewModel(private val productRepository: ProductRepository) : ViewModel() {

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

    private val _cartItems = MutableLiveData<List<Product>>()
    val cartItems: LiveData<List<Product>>
        get() = _cartItems

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
                    it.capitalize()
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

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.addProductToCart(product)
                _cartItems.postValue(_cartItems.value?.plus(product))
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to add product to cart", e)
            }
        }
    }

    fun getCartItems() {
        viewModelScope.launch {
            try {
                val cartItems = productRepository.getAllProductsFromCart()
                _cartItems.postValue(cartItems)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to load cart items", e)
            }
        }
    }
    fun removeProductFromCart(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.removeProductFromCart(product)
                _cartItems.postValue(_cartItems.value?.filter { it.id != product.id })
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to remove product from cart", e)
            }
        }
    }

}
