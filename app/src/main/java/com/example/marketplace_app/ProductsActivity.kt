package com.example.marketplace_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.adapters.CategoryAdapter
import com.example.marketplace_app.adapters.ProductAdapter
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.databinding.ActivityProductsBinding
import com.example.marketplace_app.repository.ProductRepository
import com.example.marketplace_app.viewModel.ProductsViewModel
import com.example.marketplace_app.viewModel.ProductsViewModelFactory
import kotlinx.coroutines.launch

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private var isLoading = false

    private val viewModel: ProductsViewModel by lazy {
        val productRepository = ProductRepository(ProductApi.INSTANCE)
        ViewModelProvider(this, ProductsViewModelFactory(productRepository)).get(ProductsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        setupRecyclerViewCategory()
        setupRecyclerViewProducts()
        setupSearch()
    }

    private fun setupSearch() {
        binding.editTextSearch.setOnEditorActionListener { _, _, _ ->
            val query = binding.editTextSearch.text.toString()
            viewModel.searchProducts(query)
            true
        }
    }

    private fun setupRecyclerViewCategory() {
        binding.apply {
            recyclerViewCategories.layoutManager =
                LinearLayoutManager(this@ProductsActivity, LinearLayoutManager.HORIZONTAL, false)
            categoryAdapter = CategoryAdapter() { category ->
                loadByCategory(category)
            }
            recyclerViewCategories.adapter = categoryAdapter

        }
    }

    private fun setupRecyclerViewProducts() {
        binding.apply {
            val layoutManager = GridLayoutManager(this@ProductsActivity, 2)
            recyclerViewProducts.layoutManager = layoutManager
            productAdapter = ProductAdapter() { productId ->
                onProductClick(productId)
            }
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
    }

    private fun observeViewModel() {
        viewModel.apply {
            products.observe(this@ProductsActivity) { products ->
                productAdapter.submitList(products as MutableList<Product>)
            }

            categories.observe(this@ProductsActivity) { categories ->
                categoryAdapter.submitList(categories as MutableList<String>)
            }

            searchResults.observe(this@ProductsActivity) { searchResults ->
                productAdapter.submitList(searchResults as MutableList<Product>)
            }

            isLoadingLiveData.observe(this@ProductsActivity) { isLoading ->
                // handle loading state if needed
            }

            loadProducts()
            loadCategories()
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
        viewModel.loadProductsByCategory(category)
    }

    private fun onProductClick(productId: Long) {
        val intent = Intent(this, ProductActivity::class.java).apply {
            putExtra("productId", productId)
        }
        startActivity(intent)
    }
}