package com.s24083.shoppinglist.addShoppingItem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.databinding.ActivityAddShoppingItemBinding

class AddShoppingItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddShoppingItemBinding

    private val currentShoppingItemId: Int? get() {
        val bundle: Bundle? = intent.extras
        return bundle?.getInt("id")
    }

    private val viewModel by viewModels<AddShoppingItemViewModel>{
        AddShoppingItemViewModelFactory(this, currentShoppingItemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShoppingItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.doneButton.setOnClickListener {
            addOrUpdateShoppingItem()
        }
    }

    private fun addOrUpdateShoppingItem() {
        val resultIntent = Intent()
        if (validate()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
            finish()
        }

        val name = binding.addItemName.text.toString()
        val price = binding.addItemPrice.text.toString().toDouble()
        val amount = binding.addItemAmount.text.toString().toInt()
        resultIntent.putExtra("id", currentShoppingItemId)
        resultIntent.putExtra("name", name)
        resultIntent.putExtra("price", price)
        resultIntent.putExtra("amount", amount)
        if (currentShoppingItemId == null){
            resultIntent.putExtra("isBought", false)
        } else {
            resultIntent.putExtra("isBought", false)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun validate() : Boolean {
        return binding.addItemName.text.isNullOrEmpty()
                || binding.addItemPrice.text.isNullOrEmpty()
                || binding.addItemAmount.text.isNullOrEmpty();
    }
}