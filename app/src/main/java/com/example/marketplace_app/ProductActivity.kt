package com.example.marketplace_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.adapters.ImageCarouselAdapter
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.databinding.ActivityProductBinding
import com.example.marketplace_app.repository.ProductRepository
import com.example.marketplace_app.viewModel.ProductsViewModel
import com.example.marketplace_app.viewModel.ProductsViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private val productRepository = ProductRepository(ProductApi.INSTANCE)
    private lateinit var productViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProvider(this, ProductsViewModelFactory(productRepository))
            .get(ProductsViewModel::class.java)

        binding.backButton.setOnClickListener {
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
        binding.productNameValue.text = product.name
        binding.productDescriptionText.text = product.description
        binding.productPriceValue.text = "${product.productPrice}$"
        binding.productCategoryValue.text = "Category: ${product.category.capitalize()}"
        binding.productRatingValue.text = "Rating: ${product.rating}"
        binding.productStockValue.text = "Stock: ${product.stock}"
        binding.productBrandValue.text = "Brand: ${product.brand}"
    }

    private fun setCarouselImages(images: List<String>) {
        val adapter = ImageCarouselAdapter(images)
        binding.imageCarousel.adapter = adapter
        binding.imageCarousel.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setAddToCartClickListener() {
        binding.fabAddToCart.setOnClickListener {
            // Handle click event
        }
    }

    private fun loadProduct(productId: Long) {
        productViewModel.loadProduct(productId)
        productViewModel.product.observe(this@ProductActivity) { product ->
            setProductDetails(product)
            setCarouselImages(product.images)
            setAddToCartClickListener()
        }
    }
}

