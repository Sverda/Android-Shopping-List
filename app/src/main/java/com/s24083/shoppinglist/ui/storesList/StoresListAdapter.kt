package com.s24083.shoppinglist.ui.storesList

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.data.model.Store

class StoresListAdapter(val context : Context,
                        private val onEdit: (Store) -> Unit,
                        private val onDelete: (Store) -> Unit) :
    ListAdapter<Store, StoresListAdapter.StoresListViewHolder>(StoresListDiffCallback)  {

    inner class StoresListViewHolder(
        view: View,
        onEdit: (Store) -> Unit,
        onDelete: (Store) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        private val radiusTextView: TextView = itemView.findViewById(R.id.item_radius)
        private val editButton: ImageButton = itemView.findViewById(R.id.item_edit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.item_delete)
        private val scaleUp: Animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        private val scaleDown: Animation = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        private var currentShoppingItem: Store? = null

        /* Bind item properties. */
        fun bind(item: Store) {
            currentShoppingItem = item

            nameTextView.text = "${item.name}"
            descriptionTextView.text = "${item.description}"
            radiusTextView.text = "${item.radius}m"

            val sp = context.getSharedPreferences("1", AppCompatActivity.MODE_PRIVATE)
            val detailsKey = itemView.resources.getString(R.string.settingsShowDetails)
            val detailsValue = sp.getBoolean(detailsKey, false)
            if (!detailsValue){
                descriptionTextView.visibility = View.GONE
                radiusTextView.visibility = View.GONE
            }

            val sizeKey = itemView.resources.getString(R.string.settingsTitleFontSize)
            val sizeValue = sp.getFloat(sizeKey, 1.0f)
            if (sizeValue != 1.0f){
                nameTextView.textSize *= sizeValue
            }

            editButton.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> editButton.startAnimation(scaleUp)
                    MotionEvent.ACTION_UP -> {
                        editButton.startAnimation(scaleDown)
                    }
                }

                v?.onTouchEvent(event) ?: true
            }
            editButton.setOnClickListener {
                currentShoppingItem?.let {
                    onEdit(it)
                }
            }

            deleteButton.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> deleteButton.startAnimation(scaleUp)
                    MotionEvent.ACTION_UP -> {
                        deleteButton.startAnimation(scaleDown)
                    }
                }

                v?.onTouchEvent(event) ?: true
            }
            deleteButton.setOnClickListener{
                currentShoppingItem?.let {
                    onDelete(it)
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresListAdapter.StoresListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.store_item, parent, false)
        return StoresListViewHolder(view, onEdit, onDelete)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: StoresListAdapter.StoresListViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item)
    }
}

object StoresListDiffCallback : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.id == newItem.id
    }
}