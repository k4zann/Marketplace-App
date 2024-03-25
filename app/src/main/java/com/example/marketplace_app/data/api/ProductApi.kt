package com.example.marketplace_app.data.api

import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.data.models.ProductList
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
    suspend fun getProduct(@Path("id") id: Long): Product

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductList

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{categoryName}")
    suspend fun getProductsByCategory(@Path("categoryName") categoryName: String): ProductList

}
