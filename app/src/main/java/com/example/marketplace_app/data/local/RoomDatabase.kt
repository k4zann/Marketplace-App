package com.example.marketplace_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import javax.inject.Inject

//import com.example.marketplace_app.data.local.MyConverter

@Database(entities = [CartItem::class], version = 1)
//@TypeConverter(MyConverter::class)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartItemDao(): CartItemDao

}


