package com.example.marketplace_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var productList: MutableList<Product> = mutableListOf()
    private var skip = 0
    private var limit = 20
    private var total = 0
    private var isLastElement = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        recyclerView = findViewById(R.id.recyclerViewProducts)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastElement) {
                    Log.d("ProductsActivity", "Loading more products ${visibleItemCount + firstVisibleItemPosition} $totalItemCount")
                    if (totalItemCount <= total
                    ) {
                        Log.d("ProductsActivity", "Loading more products $skip $limit")
                        loadMoreItems()
                    }
                }
            }
        })
        if (!isLastElement) {
            Log.d("ProductsActivity", "Loading products")
            loadProducts()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ProductApi.INSTANCE.getProducts(skip, limit).execute()
                }
                if (response.isSuccessful) {
                    val data = response.body()?.products ?: emptyList()
                    total = response.body()?.total ?: 0
                    productList.addAll(data)
                    limit += 20
                    skip += 20
                    productAdapter.notifyDataSetChanged()
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
                    val data = response.body()?.products ?: emptyList()
                    productList.addAll(data)
                    Log.d("ProductsActivity", "Loaded ${productList.size} more products")
                    productAdapter.notifyDataSetChanged()
                    isLoading = false
                    skip += 20
                    limit += 20
                } else {
                    Log.e("ProductsActivity", "Failed to fetch products: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ProductsActivity", "Error loading more products", e)
            }
        }
    }
}
