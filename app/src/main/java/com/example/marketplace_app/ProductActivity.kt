package com.example.marketplace_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductActivity : AppCompatActivity() {

    private val productId: EditText by lazy {findViewById(R.id.product_id_edit)}
    private val loadButton: Button by lazy {findViewById(R.id.load_button)}

    private val productTitle: TextView by lazy {findViewById(R.id.product_name_value)}
    private val productDescription: TextView by lazy{findViewById(R.id.product_description_text)}
    private val productPrice: TextView by lazy {findViewById(R.id.product_price_value)}

    private val imagePoster: ImageView by lazy {findViewById(R.id.product_image)}
    private val fab: FloatingActionButton by lazy {findViewById(R.id.fab)}
    //TODO need to add navigation from the ProductsActivity to the ProductActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        loadButton.setOnClickListener{
            val id = productId.text.toString().toLong()
            val product: Product? = null

            ProductApi.INSTANCE.getProduct(id).enqueue(callback)

            productTitle.text = product?.name ?: "Loading..."
            productDescription.text = product?.description
            productPrice.text = "${product?.productPrice.toString()}$"
        }

        fab.setOnClickListener {
            intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)
        }
    }

    fun setProduct(product: Product?) {
        productTitle.text = product?.name
        productDescription.text = product?.description
        productPrice.text = product?.productPrice.toString()


        Glide
            .with(this)
            .load(Uri.parse("https://cdn.dummyjson.com/product-images/2/3.jpg"))
            .fitCenter()
            .into(imagePoster)
    }

    private val callback = object : Callback<Product> {
        override fun onResponse(call: Call<Product>, response: Response<Product>) {
            if (response.isSuccessful) {
                val product = response.body()
                setProduct(product)
            } else {
                Toast.makeText(applicationContext, "Could not fetch the data", Toast.LENGTH_SHORT ).show()
            }
        }

        override fun onFailure(call: Call<Product>, t: Throwable) {
            Toast.makeText(applicationContext, "Could not fetch the data", Toast.LENGTH_SHORT ).show()
        }
    }
}