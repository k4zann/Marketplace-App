package com.example.marketplace_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.databinding.ItemCartBinding
import com.example.marketplace_app.ui.CartItemView
import com.example.marketplace_app.ui.adapters.diffUtil.ProductDiffCallback

class CartItemAdapter(
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, CartItemAdapter.CartItemViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartItemViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: Product) {
            binding.composeView.setContent {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        CartItemView(
                            product = cartItem,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    }
}
