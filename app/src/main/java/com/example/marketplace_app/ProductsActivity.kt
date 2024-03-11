package com.example.marketplace_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.data.ProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var productList: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            productList = fetchProductListFromApi()
            Log.d("ProductsActivity", "Product list: $productList")
            productAdapter = ProductAdapter(productList)
            recyclerView.adapter = productAdapter
        }
    }


    private suspend fun fetchProductListFromApi(): List<Product> {
        val list: List<Product> = withContext(Dispatchers.IO) {
            try {
                val response = ProductApi.INSTANCE.getProducts().execute()
                if (response.isSuccessful) {
                    response.body()?.products ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
        Log.d("ProductsActivity", "Response: $list")
        return list
    }


}