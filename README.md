# Marketplace App

The **Marketplace App** is an Android application designed to showcase products available in a marketplace. It allows users to view product details, search for products, browse categories, and add products to their cart.

## Features

- View product details including name, description, price, category, rating, stock, and brand.
- Search for products by entering keywords in the search bar.
- Browse products by category.
- Add products to the cart for purchasing.
- Scroll through images of each product using a carousel.
- Utilizes Hilt for dependency injection.
- Implements SharedPreferences for storing user preferences.
- Integrates WorkManager for background tasks.
- Utilizes BroadcastReceiver for listening to system events.
- Utilizes NotificationManager for displaying notifications.

## Technologies Used

- **Android**: The application is built using the Android framework.
- **Kotlin**: The primary programming language used for development.
- **ViewModel and LiveData**: Used for managing UI-related data in a lifecycle-conscious way.
- **RecyclerView**: Displaying a scrollable list of items efficiently.
- **Retrofit**: For making HTTP requests to the backend API.
- **Coroutines**: Used for managing asynchronous tasks.
- **Hilt**: Dependency injection library for Android.
- **SharedPreferences**: For storing user preferences locally.
- **WorkManager**: For managing background tasks efficiently.
- **BroadcastReceiver**: For listening to system events and broadcasts.
- **NotificationManager**: For displaying notifications to the user.
