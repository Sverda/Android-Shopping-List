package com.s24083.shoppinglist.addShoppingItem

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.s24083.shoppinglist.R

class AddShoppingItemActivity : AppCompatActivity() {
    private lateinit var addItemName : TextInputEditText
    private lateinit var addItemPrice : TextInputEditText
    private lateinit var addItemAmount : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shopping_item)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addShoppingItem()
        }
        addItemName = findViewById(R.id.add_item_name)
        addItemPrice = findViewById(R.id.add_item_price)
        addItemAmount = findViewById(R.id.add_item_amount)
    }

    private fun addShoppingItem() {
        val resultIntent = Intent()

        if (addItemName.text.isNullOrEmpty() || addItemPrice.text.isNullOrEmpty() || addItemAmount.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = addItemName.text.toString()
            val price = addItemPrice.text.toString().toDouble()
            val amount = addItemPrice.text.toString().toInt()
            resultIntent.putExtra("name", name)
            resultIntent.putExtra("price", price)
            resultIntent.putExtra("amount", amount)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }
}