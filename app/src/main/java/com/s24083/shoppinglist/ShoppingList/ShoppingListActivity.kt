package com.s24083.shoppinglist.ShoppingList

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.entities.ShoppingItem

class ShoppingListActivity : AppCompatActivity() {

    private val shoppingListViewModel by viewModels<ShoppingListViewModel> {
        ShoppingListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        val recyclerView: RecyclerView = findViewById(R.id.list)
        val adapter = ShoppingListAdapter()
        recyclerView.adapter = adapter

        shoppingListViewModel.allItems.observe(
            this,
            {
                it?.let {
                    adapter.submitList(it as MutableList<ShoppingItem>)
                }
            }
        )
    }

    fun fabOnClick(view: android.view.View) {
        shoppingListViewModel.insert(ShoppingItem(99, "heh", 1, 1.0, false))
        shoppingListViewModel.insert(ShoppingItem(100, "heh 2", 1, 1.0, false))
    }
}