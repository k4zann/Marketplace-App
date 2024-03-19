package com.example.marketplace_app.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.R
import com.example.marketplace_app.adapters.diffUtil.DiffCallback

class CategoryAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        val viewHolder = CategoryViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClick(currentList[position])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = currentList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = currentList.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTextView: TextView = itemView.findViewById(R.id.category_name)

        fun bind(category: String) {
            categoryNameTextView.text = category
        }

    }
}

