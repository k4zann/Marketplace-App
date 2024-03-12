package com.example.marketplace_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity() {

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private var productList: MutableList<Product> = mutableListOf()
    private var categoryList: List<String> = listOf()
    private var skip = 0
    private var limit = 20
    private var total = 0
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories)
        recyclerViewCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(categoryList) { category ->
            loadByCategory(category)
        }
        recyclerViewCategories.adapter = categoryAdapter
        val layoutManager = GridLayoutManager(this, 2)
        recyclerViewProducts.layoutManager = layoutManager
        productAdapter = ProductAdapter(productList)
        recyclerViewProducts.adapter = productAdapter
        recyclerViewProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
            }
        })
    }


    override fun onStart() {
        super.onStart()
        setupSearchView()
        loadCategories()
        loadProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ProductApi.INSTANCE.getProducts(skip, limit).execute()
                }
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    val data = productResponse?.products ?: emptyList()
                    total = productResponse?.total ?: 0
                    productList.addAll(data)
                    productAdapter.notifyDataSetChanged()
                    skip += limit
                } else {
                    Log.e("ProductsActivity", "Failed to fetch products: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error loading products", e)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadMoreItems() {
        isLoading = true
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ProductApi.INSTANCE.getProducts(skip, limit).execute()
                }
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    val data = productResponse?.products ?: emptyList()
                    productList.addAll(data)
                    productAdapter.notifyDataSetChanged()
                    isLoading = false
                    skip += limit
                } else {
                    Log.e("ProductsActivity", "Failed to fetch products: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error loading more products", e)
            }
        }
    }

    private fun setupSearchView() {
        val searchView = findViewById<EditText>(R.id.editTextSearch)
        searchView.setOnEditorActionListener { _, _, _ ->
            val query = searchView.text.toString()
            performSearch(query)
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun performSearch(query: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ProductApi.INSTANCE.searchProducts(query).execute()
                }
                if (response.isSuccessful) {
                    val data = response.body()?.products ?: emptyList()
                    total = response.body()?.total ?: 0
                    productAdapter.updateProducts(data.toMutableList())
                    productAdapter.notifyDataSetChanged()
                } else {
                    Log.e("ProductsActivity", "Failed to fetch search results: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error searching products", e)
            }
        }
    }


    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ProductApi.INSTANCE.getCategories().execute()
                }
                if (response.isSuccessful) {
                    var categories = response.body() ?: emptyList()
                    categories = categories.map { it.capitalize() }
                    categories = listOf("All") + categories
                    Log.d("ProductsActivity", "Categories: $categories")
                    categoryAdapter.setCategories(categories)
                } else {
                    Log.e("ProductsActivity", "Failed to fetch categories: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error loading categories", e)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadByCategory(category: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    if (category == "All") {
                        ProductApi.INSTANCE.getProducts(0, limit).execute()
                    } else {
                        ProductApi.INSTANCE.getProductsByCategory(category).execute()
                    }
                }
                if (response.isSuccessful) {
                    val data = response.body()?.products ?: emptyList()
                    total = response.body()?.total ?: 0
                    productAdapter.updateProducts(data.toMutableList())
                    productAdapter.notifyDataSetChanged()
                } else {
                    Log.e("ProductsActivity", "Failed to fetch products by category: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error loading products by category", e)
            }
        }
    }

}
