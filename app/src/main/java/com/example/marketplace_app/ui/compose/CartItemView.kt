package com.example.marketplace_app.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.R

@Composable
fun CartItemView(
    product: Product,
    onItemClick: (Product) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(product) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImage(
            imageUrl = product.poster,
            contentDescription = "Product Image",
            modifier = Modifier
                .size(64.dp),
            alignment = Alignment.Center
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${product.productPrice}",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun LoadImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Int = 64,
    alignment: Alignment
) {
    val painter: Painter = rememberImagePainter(imageUrl)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.size(size.dp),
        alignment = alignment
    )
}