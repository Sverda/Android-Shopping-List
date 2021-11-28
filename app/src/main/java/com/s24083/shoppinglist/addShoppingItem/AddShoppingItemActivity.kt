package com.s24083.shoppinglist.addShoppingItem

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.s24083.shoppinglist.R

class AddShoppingItemActivity : AppCompatActivity() {
    private lateinit var addItemName : TextInputEditText
    private lateinit var addItemPrice : TextInputEditText
    private lateinit var addItemAmount : TextInputEditText

    private val currentShoppingItemId: Int? get() {
        val bundle: Bundle? = intent.extras
        return bundle?.getInt("id")
    }

    private val viewModel by viewModels<AddShoppingItemViewModel>{
        AddShoppingItemViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shopping_item)

        findViewById<MaterialButton>(R.id.done_button).setOnClickListener {
            addShoppingItem()
        }
        addItemName = findViewById(R.id.add_item_name)
        addItemPrice = findViewById(R.id.add_item_price)
        addItemAmount = findViewById(R.id.add_item_amount)

        currentShoppingItemId?.let {
            val currentItem = viewModel.getItemForId(currentShoppingItemId!!)
            addItemName.setText(currentItem.name)
            addItemPrice.setText(currentItem.price.toString())
            addItemAmount.setText(currentItem.amount.toString())
        }
    }

    private fun addShoppingItem() {
        val resultIntent = Intent()

        if (addItemName.text.isNullOrEmpty() || addItemPrice.text.isNullOrEmpty() || addItemAmount.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = addItemName.text.toString()
            val price = addItemPrice.text.toString().toDouble()
            val amount = addItemAmount.text.toString().toInt()
            resultIntent.putExtra("id", currentShoppingItemId)
            resultIntent.putExtra("name", name)
            resultIntent.putExtra("price", price)
            resultIntent.putExtra("amount", amount)
            if (currentShoppingItemId == null){
                resultIntent.putExtra("isBought", false)
            }
            else {
                val currentItem = viewModel.getItemForId(currentShoppingItemId!!)
                resultIntent.putExtra("isBought", currentItem.isBought)
            }
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }
}