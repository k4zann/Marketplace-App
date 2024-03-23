package com.example.marketplace_app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.databinding.ItemCartBinding
import com.example.marketplace_app.ui.adapters.diffUtil.ProductDiffCallback

class CartItemAdapter : ListAdapter<Product, CartItemAdapter.CartItemViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartItemViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cartItem: Product) {
            binding.apply {
                textViewTitle.text = cartItem.name
                textViewPrice.text = "$${cartItem.productPrice}"

                Glide.with(itemView)
                    .load(cartItem.poster)
                    .into(imageViewThumbnail)
            }
        }
    }
}
