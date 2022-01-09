package com.s24083.shoppinglist.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.data.LoginCache
import com.s24083.shoppinglist.ui.login.LoginActivity
import com.s24083.shoppinglist.ui.settings.OptionsActivity
import com.s24083.shoppinglist.ui.shoppingList.ShoppingListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MainViewModelFactory())
            .get(MainViewModel::class.java)
    }

    fun goToList(view: View){
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }

    fun goToSettings(view: View){
        val intent = Intent(this, OptionsActivity::class.java)
        startActivity(intent)
    }

    fun logout(view: View){
        viewModel.logout()
        val cache = LoginCache(applicationContext)
        cache.Clean()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}