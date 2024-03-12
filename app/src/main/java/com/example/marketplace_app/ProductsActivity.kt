package com.example.marketplace_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.adapters.ProductAdapter
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.repository.ProductRepository
import com.example.marketplace_app.viewModel.ProductsViewModel
import com.example.marketplace_app.viewModel.ProductsViewModelFactory
import kotlinx.coroutines.launch

class ProductsActivity : AppCompatActivity() {

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var searchEditText: EditText

    private var skip = 0
    private var limit = 20
    private var total = 0
    private var isLoading = false


    private val viewModel: ProductsViewModel by lazy {
        val productRepository = ProductRepository(ProductApi.INSTANCE)
        ViewModelProvider(this, ProductsViewModelFactory(productRepository)).get(ProductsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        initViews()
        setupSearch()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun initViews() {
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories)
        searchEditText = findViewById(R.id.editTextSearch)
    }

    private fun setupSearch() {
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString()
            lifecycleScope.launch {
                viewModel.searchProducts(query)
            }
            true
        }
    }

    private fun setupRecyclerViews() {
        recyclerViewCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            loadByCategory(category)
        }
        recyclerViewCategories.adapter = categoryAdapter

        val layoutManager = GridLayoutManager(this, 2)
        recyclerViewProducts.layoutManager = layoutManager
        productAdapter = ProductAdapter(mutableListOf())
        recyclerViewProducts.adapter = productAdapter

        recyclerViewProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == productAdapter.itemCount - 1) {
                    loadMoreItems()
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.products.observe(this) { products ->
            productAdapter.updateProducts(products as MutableList<Product>)
        }

        viewModel.categories.observe(this) { categories ->
            categoryAdapter.setCategories(categories)
        }

        viewModel.searchResults.observe(this) { searchResults ->
            productAdapter.updateProducts(searchResults as MutableList<Product>)
        }

        lifecycleScope.launch {
            viewModel.loadProducts()
            viewModel.loadCategories()
        }
    }

    private fun loadMoreItems() {
        if (isLoading) return
        isLoading = true
        lifecycleScope.launch {
            viewModel.loadMoreProducts()
            isLoading = false
        }
    }

    private fun loadByCategory(category: String) {
        lifecycleScope.launch {
            viewModel.loadProductsByCategory(category)
        }
    }
}
