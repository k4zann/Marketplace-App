package com.example.marketplace_app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marketplace_app.data.local.CartDatabase
import com.example.marketplace_app.data.local.CartItem
import com.example.marketplace_app.data.local.CartItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class CartItemDaoTest {

    private lateinit var cartItemDao: CartItemDao
    private lateinit var cartDatabase: CartDatabase


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        cartDatabase = Room.inMemoryDatabaseBuilder(context, CartDatabase::class.java).build()
        cartItemDao = cartDatabase.cartItemDao()
    }

    @After
    fun teardown() {
        cartDatabase.close()
    }

    @Test
    suspend fun testInsertCartItem() {
        val cartItem = CartItem(
            productId = 123,
            title = "Sample Product",
            description = "Description of the product",
            price = 100,
            thumbnail = "thumbnail_url",
            category = "Sample Category",
            rating = 4.5f,
            stock = 10,
            brand = "Sample Brand"
        )
        withContext(Dispatchers.IO) {
            cartItemDao.insertCartItem(cartItem)
        }


        val allCartItems = cartItemDao.getAllCartItems()
        assertEquals(1, allCartItems.size)
        assertEquals(cartItem, allCartItems[0])
    }

}

