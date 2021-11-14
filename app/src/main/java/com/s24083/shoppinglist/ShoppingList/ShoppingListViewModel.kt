package com.s24083.shoppinglist.ShoppingList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s24083.shoppinglist.entities.ShoppingItem
import com.s24083.shoppinglist.repositories.ShoppingItemRepository

class ShoppingListViewModel(private val repository: ShoppingItemRepository)
    : ViewModel() {
    val allItems: LiveData<List<ShoppingItem>> = repository.allItems
    fun insert(item: ShoppingItem) = repository.insert(item)
    fun update(item: ShoppingItem) = repository.update(item)
    fun delete(item: ShoppingItem) = repository.delete(item)
}

class ShoppingListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingListViewModel(
                ShoppingItemRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}