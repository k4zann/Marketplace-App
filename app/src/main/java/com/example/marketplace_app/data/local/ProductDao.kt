package com.example.marketplace_app.data.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartItemDao {
    @Insert
    suspend fun insertCartItem(entity: CartItem)

    @Delete
    suspend fun deleteCartItem(entity: CartItem)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>


}