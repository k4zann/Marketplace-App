package com.example.marketplace_app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace_app.adapters.diffUtil.DiffCallback
import com.example.marketplace_app.R
import com.example.marketplace_app.data.Product


class ProductAdapter(
    private val onClickItem: (Long) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        val viewHolder = ProductViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            onClickItem.invoke(getItem(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    override fun getItemCount(): Int = currentList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.textViewTitle)
        private val productPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val productImage: ImageView = itemView.findViewById(R.id.imageViewThumbnail)


        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = "${product.productPrice}$"
            Glide.with(itemView.context)
                .load(product.poster)
                .centerCrop()
                .into(productImage)
        }
    }
}


