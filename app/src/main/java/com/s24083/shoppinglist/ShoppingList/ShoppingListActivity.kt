package com.s24083.shoppinglist.ShoppingList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.addShoppingItem.AddShoppingItemActivity
import com.s24083.shoppinglist.entities.ShoppingItem


class ShoppingListActivity : AppCompatActivity() {

    private val shoppingListViewModel by viewModels<ShoppingListViewModel> {
        ShoppingListViewModelFactory(this)
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val name = result.data?.getStringExtra("name")
                var price = result.data?.getDoubleExtra("price", 0.0)
                val amount = result.data?.getIntExtra("amount", 0)
                val maxId = shoppingListViewModel.allItems.value?.maxByOrNull { i -> i.id }?.id ?: 0
                val item = ShoppingItem(maxId + 1 , name ?: "", amount ?: 0, price ?: 0.0, false)
                shoppingListViewModel.insert(item)
            }
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
                    adapter.submitList(null)
                    adapter.submitList(it)
                }
            }
        )
    }

    fun fabOnClick(view: android.view.View) {
        val intent = Intent(this, AddShoppingItemActivity::class.java)
        intentLauncher.launch(intent)
    }
}