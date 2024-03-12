package com.example.marketplace_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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


    fun loadProducts() {
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = productRepository.getProducts(_products.value?.size ?: 0, 20)
                _products.postValue(_products.value?.plus(products))
            } catch (e: Exception) {
                Log.d("ProductsViewModel", "Failed to load more products", e)
            }
        }
    }

    fun loadProduct(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categories = productRepository.getCategories()
                _categories.postValue(categories)
            } catch (e: Exception) {
                // Handle error
                Log.d("ProductsViewModel", "Failed to load categories", e)
            }
        }
    }

    fun searchProducts(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
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
