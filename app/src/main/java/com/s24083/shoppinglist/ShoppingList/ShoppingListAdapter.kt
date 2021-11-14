package com.s24083.shoppinglist.ShoppingList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.entities.ShoppingItem

class ShoppingListAdapter() :
    ListAdapter<ShoppingItem, ShoppingListAdapter.ShoppingListViewHolder>(ShoppingListDiffCallback) {

    inner class ShoppingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        private val amountTextView: TextView = itemView.findViewById(R.id.item_amount)
        private val isBoughtCheckBoxView: CheckBox = itemView.findViewById(R.id.item_isBought)
        private var currentShoppingItem: ShoppingItem? = null

        /* Bind item properties. */
        fun bind(item: ShoppingItem) {
            currentShoppingItem = item

            nameTextView.text = item.name
            priceTextView.text = item.price.toString()
            amountTextView.text = item.amount.toString()
            isBoughtCheckBoxView.isChecked = item.isBought
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_item, parent, false)
        return ShoppingListViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ShoppingListViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item)
    }
}

object ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.id == newItem.id
    }
}