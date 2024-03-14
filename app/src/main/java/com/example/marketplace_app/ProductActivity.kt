package com.example.marketplace_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.adapters.ImageCarouselAdapter
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.repository.ProductRepository
import com.example.marketplace_app.viewModel.ProductsViewModel
import com.example.marketplace_app.viewModel.ProductsViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ProductActivity : AppCompatActivity() {

    private lateinit var productTitle: TextView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView
    private lateinit var productCategory: TextView
    private lateinit var productRating: TextView
    private lateinit var productStock: TextView
    private lateinit var productBrand: TextView
    private lateinit var imageCarousel: RecyclerView
    private lateinit var fabAddToCart: FloatingActionButton
    private lateinit var backButton: Button

    private val productRepository = ProductRepository(ProductApi.INSTANCE)
    private lateinit var productViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        productViewModel = ViewModelProvider(this, ProductsViewModelFactory(productRepository)).get(ProductsViewModel::class.java)
        productTitle = findViewById(R.id.product_name_value)
        productDescription = findViewById(R.id.product_description_text)
        productPrice = findViewById(R.id.product_price_value)
        productCategory = findViewById(R.id.product_category_value)
        productRating = findViewById(R.id.product_rating_value)
        productStock = findViewById(R.id.product_stock_value)
        productBrand = findViewById(R.id.product_brand_value)
        imageCarousel = findViewById(R.id.image_carousel)
        fabAddToCart = findViewById(R.id.fab_add_to_cart)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        val productId = intent.getLongExtra("productId", -1L)
        if (productId != -1L) {
            loadProduct(productId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductDetails(product: Product) {
        productTitle.text = product.name
        productDescription.text = product.description
        productPrice.text = "${product.productPrice}$"
        productCategory.text = "Category: ${product.category.capitalize()}"
        productRating.text = "Rating: ${product.rating}"
        productStock.text = "Stock: ${product.stock}"
        productBrand.text = "Brand: ${product.brand}"

    }

    private fun setCarouselImages(images: List<String>) {
        val adapter = ImageCarouselAdapter(images)
        imageCarousel.adapter = adapter
        imageCarousel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setAddToCartClickListener() {
        fabAddToCart.setOnClickListener {
        }
    }

    private fun loadProduct(productId: Long) {
        lifecycleScope.launch {
            productViewModel.loadProduct(productId)
            productViewModel.product.observe(this@ProductActivity) { product ->
                setProductDetails(product)
                setCarouselImages(product.images)
                setAddToCartClickListener()
            }
        }
    }
}
