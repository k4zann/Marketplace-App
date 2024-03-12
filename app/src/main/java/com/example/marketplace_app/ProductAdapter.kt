package com.example.marketplace_app

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace_app.data.Product


class ProductAdapter(
    private val productList: List<Product>,
    ) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
            R.layout.product_item,
            parent,
            false
        )
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.titleTextView.text = product.name
//        holder.descriptionTextView.text = product.description

        Glide.with(holder.itemView.context)
            .load(Uri.parse(product.poster))
            .fitCenter()
            .centerCrop()
            .into(holder.thumbnailImageView)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.imageViewThumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
    }
}

