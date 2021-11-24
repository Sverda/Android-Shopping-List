package com.s24083.shoppinglist.ShoppingList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.entities.ShoppingItem

class ShoppingListAdapter(val context : Context,
                          private val onEdit: (ShoppingItem) -> Unit,
                          private val onDelete: (ShoppingItem) -> Unit) :
    ListAdapter<ShoppingItem, ShoppingListAdapter.ShoppingListViewHolder>(ShoppingListDiffCallback) {

    inner class ShoppingListViewHolder(
        view: View,
        onEdit: (ShoppingItem) -> Unit,
        onDelete: (ShoppingItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        private val amountTextView: TextView = itemView.findViewById(R.id.item_amount)
        private val isBoughtCheckBoxView: CheckBox = itemView.findViewById(R.id.item_isBought)
        private val editButton: ImageButton = itemView.findViewById(R.id.item_edit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.item_delete)
        private var currentShoppingItem: ShoppingItem? = null

        /* Bind item properties. */
        fun bind(item: ShoppingItem) {
            currentShoppingItem = item

            nameTextView.text = "${item.name}"
            amountTextView.text = "x${item.amount}"
            priceTextView.text = "${item.price}$ "
            isBoughtCheckBoxView.isChecked = item.isBought

            val sp = context.getSharedPreferences("1", AppCompatActivity.MODE_PRIVATE)
            val detailsKey = itemView.resources.getString(R.string.settingsShowDetails)
            val detailsValue = sp.getBoolean(detailsKey, false)
            if (!detailsValue){
                amountTextView.visibility = View.GONE
                priceTextView.visibility = View.GONE
            }

            val sizeKey = itemView.resources.getString(R.string.settingsTitleFontSize)
            val sizeValue = sp.getFloat(sizeKey, 1.0f)
            if (sizeValue != 1.0f){
                nameTextView.textSize *= sizeValue
            }

            editButton.setOnClickListener {
                currentShoppingItem?.let {
                    onEdit(it)
                }
            }
            deleteButton.setOnClickListener{
                currentShoppingItem?.let {
                    onDelete(it)
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_item, parent, false)
        return ShoppingListViewHolder(view, onEdit, onDelete)
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