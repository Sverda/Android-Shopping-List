package com.s24083.shoppinglist.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.s24083.shoppinglist.ui.addShoppingItem.AddShoppingItemActivity

private const val TAG = "AddItemBroadcastReceiver"

class AddItemBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val editItemIntent = Intent(context, AddShoppingItemActivity::class.java)
        editItemIntent.putExtra("id", intent.getIntExtra("id", 0))
        context.startActivity(editItemIntent)
    }
}