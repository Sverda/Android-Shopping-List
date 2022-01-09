package com.s24083.shoppinglist.ui.storesList

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.data.model.ShoppingItem
import com.s24083.shoppinglist.data.model.Store
import com.s24083.shoppinglist.receivers.AddItemBroadcastReceiver
import com.s24083.shoppinglist.ui.addShoppingItem.AddShoppingItemActivity
import com.s24083.shoppinglist.ui.addStore.AddStoreActivity
import com.s24083.shoppinglist.ui.shoppingList.ShoppingListAdapter
import com.s24083.shoppinglist.ui.storesMap.StoresMapActivity

class StoresListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private val listViewModel by viewModels<StoresListViewModel> {
        StoresListViewModelFactory(this)
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val existingId = result.data?.getIntExtra("id", -1)
                val name = result.data?.getStringExtra("name")
                val description = result.data?.getStringExtra("description")
                val radius = result.data?.getIntExtra("radius", 0)
                val location = result.data?.getStringExtra("location")
                if (existingId == -1) {
                    val maxId = listViewModel.allItems.value?.maxByOrNull { i -> i.id }?.id ?: 0
                    val item = Store(
                        maxId + 1 ,
                        name ?: "",
                        description ?: "",
                        radius ?: 0,
                        location ?: "")
                    listViewModel.insert(item)
                    broadcastItemCreation(item)
                }
                else {
                    val isBought = result.data?.getBooleanExtra("isBought", false)
                    val item = Store(
                        existingId ?: throw Exception("invalid id"),
                        name ?: "",
                        description ?: "",
                        radius ?: 0,
                        location ?: "")
                    listViewModel.update(item)
                    recyclerView?.recycledViewPool?.clear()
                    recyclerView?.adapter?.notifyItemChanged(item.id)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores_list)

        recyclerView = findViewById(R.id.list)
        val adapter = StoresListAdapter(this, {item -> onItemEdit(item)},{ item -> onItemDelete(item)})
        recyclerView?.adapter = adapter

        run {
            listViewModel.allItems.observe(
                this,
                {
                    it?.let {
                        adapter.submitList(null)

                        adapter.submitList(it)
                    }
                }
            )
        }

        val receiver = AddItemBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction("com.s24083.shoppinglist.STORE_ADDED_RESPONSE")
        registerReceiver(receiver, filter)
    }

    private fun onItemEdit(item: Store) {
        val intent = Intent(this, AddStoreActivity::class.java)
        intent.putExtra("id", item.id)
        intentLauncher.launch(intent)
    }

    private fun onItemDelete(item: Store) {
        listViewModel.delete(item)
        recyclerView?.recycledViewPool?.clear()
        recyclerView?.adapter?.notifyItemRemoved(item.id)
    }

    fun showMap(view: android.view.View) {
        val intent = Intent(this, StoresMapActivity::class.java)
        intentLauncher.launch(intent)
    }

    fun addStore(view: android.view.View) {
        val intent = Intent(this, AddStoreActivity::class.java)
        intentLauncher.launch(intent)
    }

    fun broadcastItemCreation(item: Store){
        Intent("com.s24083.shoppinglist.STORE_ADDED").also { intent ->
            intent.putExtra("id", item.id)
            intent.putExtra("name", item.name)
            sendBroadcast(intent)
        }
    }
}