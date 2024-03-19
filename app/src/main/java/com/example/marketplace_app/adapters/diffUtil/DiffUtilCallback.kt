package com.example.marketplace_app.adapters.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.marketplace_app.data.Product

class DiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}