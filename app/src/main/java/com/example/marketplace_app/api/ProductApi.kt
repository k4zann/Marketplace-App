package com.example.marketplace_app.api

import com.example.marketplace_app.data.Product
import com.example.marketplace_app.data.ProductList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// везде suspend fun, и убираешь Call, возвращаешь сам тип
interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductList

    @GET("products/{id}")
    fun getProduct(@Path("id") id: Long): Call<Product>

    @GET("products/search")
    fun searchProducts(@Query("q") query: String): Call<ProductList>

    @GET("products/categories")
    fun getCategories(): Call<List<String>>

    @GET("products/category/{categoryName}")
    fun getProductsByCategory(@Path("categoryName") categoryName: String): Call<ProductList>

    companion object {
        // посмотри на buildGradle в buildTypes из пятого урока где объявляю ссылку
        // погугли насчет как пользоваться BuildConfig, какие настройки нужны поставить в градле
        val INSTANCE: ProductApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dummyjson.com/")
            .build()
            .create(ProductApi::class.java)
    }
}
