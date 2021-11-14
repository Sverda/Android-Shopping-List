package com.s24083.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.s24083.shoppinglist.Settings.OptionsActivity
import com.s24083.shoppinglist.ShoppingList.ShoppingListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToList(view: View){
        var intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }

    fun goToSettings(view: View){
        val intent = Intent(this, OptionsActivity::class.java)
        startActivity(intent)
    }
}