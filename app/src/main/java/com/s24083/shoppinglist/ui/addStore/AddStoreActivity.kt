package com.s24083.shoppinglist.ui.addStore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.s24083.shoppinglist.databinding.ActivityAddStoreBinding

class AddStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoreBinding

    private val currentStoreId: Int? get() {
        val bundle: Bundle? = intent.extras
        return bundle?.getInt("id")
    }

    private val viewModel by viewModels<AddStoreViewModel>{
        AddStoreViewModelFactory(this, currentStoreId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.doneButton.setOnClickListener {
            addOrUpdateStore()
        }
    }

    private fun addOrUpdateStore() {
        val resultIntent = Intent()
        if (validate()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
            finish()
        }

        val name = binding.addStoreName.text.toString()
        val description = binding.addStoreDescription.text.toString()
        val radius = binding.addStoreRadius.text.toString().toInt()
        resultIntent.putExtra("id", currentStoreId)
        resultIntent.putExtra("name", name)
        resultIntent.putExtra("description", description)
        resultIntent.putExtra("radius", radius)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun validate() : Boolean {
        return binding.addStoreName.text.isNullOrEmpty()
                || binding.addStoreDescription.text.isNullOrEmpty()
                || binding.addStoreRadius.text.isNullOrEmpty();
    }
}