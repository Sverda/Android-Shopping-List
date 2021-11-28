package com.s24083.shoppinglist.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

private const val TAG = "AddItemBroadcastReceiver"

class AddItemBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent?.action
        val itemName = intent?.getStringExtra("name")
//        Toast.makeText(context, "$itemName item was added", Toast.LENGTH_LONG).show()
    }
}