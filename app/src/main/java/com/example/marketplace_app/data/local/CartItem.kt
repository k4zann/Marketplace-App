package com.example.marketplace_app.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

@Entity(tableName = "cart_items")
data class CartItem @Inject constructor(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "stock")
    val stock: Int,
    @ColumnInfo(name = "brand")
    val brand: String,
    @ColumnInfo(name="quantity")
    val quantity: Int
)