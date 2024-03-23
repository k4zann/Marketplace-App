package com.example.marketplace_app.data.local


import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartItemDao {
    @Insert
    suspend fun insertCartItem(entity: CartItem) {
        Log.d("CartItemDao", "insertCartItem: $entity")
    }

    @Delete
    suspend fun deleteCartItem(entity: CartItem)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>


}