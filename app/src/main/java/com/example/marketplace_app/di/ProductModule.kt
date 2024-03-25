package com.example.marketplace_app.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.example.marketplace_app.data.api.ProductApi
import com.example.marketplace_app.data.local.CartDatabase
import com.example.marketplace_app.data.local.CartItemDao
import com.example.marketplace_app.data.repository.CartRepository
import com.example.marketplace_app.data.repository.ProductRepository
import com.example.marketplace_app.utils.notification.ProductNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductModule {

    @Provides
    @Singleton
    fun provideProductNotificationManager(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): ProductNotificationManager {
        return ProductNotificationManager(context, notificationManager)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideProductApiService(): ProductApi {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }


    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "marketplace_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(cartDatabase: CartDatabase) = cartDatabase.cartItemDao()


    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository {
        return ProductRepository(productApi)
    }


    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartItemDao): CartRepository {
        return CartRepository(cartDao)
    }

}