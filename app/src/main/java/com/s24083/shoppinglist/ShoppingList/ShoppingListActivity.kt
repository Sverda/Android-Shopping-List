package com.s24083.shoppinglist.ShoppingList

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.addShoppingItem.AddShoppingItemActivity
import com.s24083.shoppinglist.entities.ShoppingItem
import com.s24083.shoppinglist.receivers.AddItemBroadcastReceiver


class ShoppingListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private val shoppingListViewModel by viewModels<ShoppingListViewModel> {
        ShoppingListViewModelFactory(this)
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val existingId = result.data?.getIntExtra("id", -1)
                val name = result.data?.getStringExtra("name")
                val price = result.data?.getDoubleExtra("price", 0.0)
                val amount = result.data?.getIntExtra("amount", 0)
                if (existingId == -1) {
                    val maxId = shoppingListViewModel.allItems.value?.maxByOrNull { i -> i.id }?.id ?: 0
                    val item = ShoppingItem(maxId + 1 , name ?: "", amount ?: 0, price ?: 0.0, false)
                    shoppingListViewModel.insert(item)
                    broadcastItemCreation(item)
                }
                else {
                    val isBought = result.data?.getBooleanExtra("isBought", false)
                    val item = ShoppingItem(
                        existingId ?: throw Exception("invalid id") ,
                        name ?: "",
                        amount ?: 0,
                        price ?: 0.0,
                        isBought ?: false)
                    shoppingListViewModel.update(item)
                    recyclerView?.recycledViewPool?.clear()
                    recyclerView?.adapter?.notifyItemChanged(item.id)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        recyclerView = findViewById(R.id.list)
        val adapter = ShoppingListAdapter(this, {item -> onItemEdit(item)},{ item -> onItemDelete(item)})
        recyclerView?.adapter = adapter

        shoppingListViewModel.allItems.observe(
            this,
            {
                it?.let {
                    adapter.submitList(null)
                    adapter.submitList(it)
                }
            }
        )

        val receiver = AddItemBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction("com.s24083.shoppinglist.ITEM_ADDED_RESPONSE")
        registerReceiver(receiver, filter)
    }

    private fun onItemEdit(item: ShoppingItem) {
        val intent = Intent(this, AddShoppingItemActivity::class.java)
        intent.putExtra("id", item.id)
        intentLauncher.launch(intent)
    }

    private fun onItemDelete(item: ShoppingItem) {
        shoppingListViewModel.delete(item)
        recyclerView?.recycledViewPool?.clear()
        recyclerView?.adapter?.notifyItemRemoved(item.id)
    }

    fun fabOnClick(view: android.view.View) {
        val intent = Intent(this, AddShoppingItemActivity::class.java)
        intentLauncher.launch(intent)
    }

    fun broadcastItemCreation(item: ShoppingItem){
        Intent("com.s24083.shoppinglist.ITEM_ADDED").also { intent ->
            intent.putExtra("id", item.id)
            intent.putExtra("name", item.name)
            sendBroadcast(intent)
        }
    }
}