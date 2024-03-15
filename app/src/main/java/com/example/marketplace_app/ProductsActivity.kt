package com.example.marketplace_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
import com.example.marketplace_app.repository.ProductRepository
import com.example.marketplace_app.viewModel.ProductsViewModel
import com.example.marketplace_app.viewModel.ProductsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Отображай на Fragment
class ProductsActivity : AppCompatActivity() {

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var searchEditText: EditText

    private var isLoading = false

    private val mainScope = CoroutineScope(Dispatchers.Main)

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
        // заменить на viewbinding везде
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
        //debounce with edittext and coroutine
//        searchEditText.addTextChangedListener(object : TextWatcher {
//            private var searchFor = ""
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val searchText = s.toString().trim()
//                if (searchText == searchFor) {
//                    return
//                }
//                searchFor = searchText
//                mainScope.launch {
//                    delay(500)
//                    if (searchText != searchFor)
//                        return@launch
//                    viewModel.searchProducts(searchText)
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//        })
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
        productAdapter = ProductAdapter(mutableListOf(), ::onProductClick)
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
            categoryAdapter.submitList(categories)
        }

        viewModel.searchResults.observe(this) { searchResults ->
            productAdapter.updateProducts(searchResults as MutableList<Product>)
        }

        viewModel.isLoadingLiveData.observe(this) { isLoading ->
            // здесь логика
        }
        viewModel.loadProducts()
        viewModel.loadCategories()
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
    private fun onProductClick(productId: Long) {
        val intent = Intent(this, ProductActivity::class.java).apply {
            putExtra("productId", productId)
        }
        startActivity(intent)
    }
}
