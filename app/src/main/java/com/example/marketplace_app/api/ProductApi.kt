package com.example.marketplace_app.api

import com.example.marketplace_app.data.Product
import com.example.marketplace_app.data.ProductList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    fun getProducts(): Call<ProductList>


    @GET("products/{id}")
    fun getProduct(@Path("id") id: Long): Call<Product>

    companion object {
        val INSTANCE = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dummyjson.com/")
            .build()
            .create(ProductApi::class.java)
    }
}