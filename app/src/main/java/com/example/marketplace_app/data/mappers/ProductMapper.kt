package com.example.marketplace_app.data.mappers

import com.example.marketplace_app.data.local.CartItem
import com.example.marketplace_app.data.models.Product

internal fun CartItem.toPresentation() =
    Product(
        id = id,
        name = title,
        productPrice = price,
        description = description,
        poster = thumbnail,
        category = category,
        rating = rating,
        stock = stock,
        brand = brand,
        images = null
    )

internal fun Product.toEntity() =
    CartItem(
        id = id,
        productId = id,
        title = name,
        description = description,
        price = productPrice,
        thumbnail = poster,
        category = category,
        rating = rating,
        stock = stock,
        brand = brand
    )