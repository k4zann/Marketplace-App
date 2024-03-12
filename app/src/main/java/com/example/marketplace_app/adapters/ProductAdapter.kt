// ProductAdapter.kt
package com.example.marketplace_app.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace_app.DiffCallback
import com.example.marketplace_app.ProductActivity
import com.example.marketplace_app.R
import com.example.marketplace_app.data.Product

class ProductAdapter(
    private var productList: MutableList<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    fun updateProducts(newProducts: MutableList<Product>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(productList, newProducts))
        productList.clear()
        productList.addAll(newProducts)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.textViewTitle)
        private val productPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val productImage: ImageView = itemView.findViewById(R.id.imageViewThumbnail)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList[position]
                    val context = itemView.context
                    val intent = Intent(context, ProductActivity::class.java).apply {
                        putExtra("productId", product.id)
                    }
                    context.startActivity(intent)
                }
            }
        }

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
