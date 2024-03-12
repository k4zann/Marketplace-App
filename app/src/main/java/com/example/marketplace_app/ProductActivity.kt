package com.example.marketplace_app

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.marketplace_app.data.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

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
        // Fetch product details
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
            // Add product to cart
        }
    }

    private fun loadProduct(productId: Long) {
        val call = ProductApi.INSTANCE.getProduct(productId)
        call.enqueue(callback)
    }

    private val callback = object : Callback<Product> {
        override fun onResponse(call: Call<Product>, response: Response<Product>) {
            if (response.isSuccessful) {
                val product = response.body()
                product?.let {
                    setProductDetails(it)
                    setCarouselImages(it.images.subList(1, it.images.size-1))
                    setAddToCartClickListener()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Failed to fetch product details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<Product>, t: Throwable) {
            Toast.makeText(
                applicationContext,
                "Failed to fetch product details: ${t.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
