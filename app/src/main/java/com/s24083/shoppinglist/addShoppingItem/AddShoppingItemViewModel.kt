package com.s24083.shoppinglist.addShoppingItem

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s24083.shoppinglist.entities.ShoppingItem
import com.s24083.shoppinglist.repositories.ShoppingItemRepository

class AddShoppingItemViewModel(private val repository: ShoppingItemRepository)
    : ViewModel() {

    fun getItemForId(id: Int) : ShoppingItem {
        return repository.allItems.find { i -> i.id == id } ?: throw Exception("item with id $id not found")
    }
}

class AddShoppingItemViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddShoppingItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddShoppingItemViewModel(
                ShoppingItemRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}