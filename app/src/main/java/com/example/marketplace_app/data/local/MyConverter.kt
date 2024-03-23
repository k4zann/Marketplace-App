package com.example.marketplace_app.data.local

import androidx.room.TypeConverter
import com.example.marketplace_app.data.models.Product
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class MyConverter {
//    private val json = Json { ignoreUnknownKeys = true }
//
//    @TypeConverter
//    fun fromProductList(products: List<Product>?): String {
//        if (products == null) return ""
//        return json.encodeToString(ListSerializer(Product.serializer()), products)
//    }
//
//    @TypeConverter
//    fun toProductList(productString: String?): List<Product> {
//        if (productString.isNullOrEmpty()) return emptyList()
//        return json.decodeFromString(productString)
//    }
}