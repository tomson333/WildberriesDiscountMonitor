package com.example.wbmonitor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wbmonitor.R
import com.example.wbmonitor.data.Product

class ProductAdapter(
    private var items: MutableList<Product>,
    private val onDelete: (Product) -> Unit,
    private val onRefresh: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val url: TextView = view.findViewById(R.id.itemUrl)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val refreshButton: ImageButton = view.findViewById(R.id.refreshButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]
        holder.title.text = if (product.title.isNotEmpty()) product.title else "Товар"
        holder.url.text = product.url
        holder.price.text = if (product.lastKnownPrice >= 0) product.lastKnownPrice.toString() else "—"

        holder.deleteButton.setOnClickListener { onDelete(product) }
        holder.refreshButton.setOnClickListener { onRefresh(product) }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Product>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
