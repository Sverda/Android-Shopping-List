package com.s24083.shoppinglist.ui.shoppingList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s24083.shoppinglist.data.model.ShoppingItem
import com.s24083.shoppinglist.data.ShoppingListRepository

class ShoppingListViewModel(private val repository: ShoppingListRepository)
    : ViewModel() {
    val allItems: MutableLiveData<MutableList<ShoppingItem>> = repository.getItems()

    fun insert(item: ShoppingItem){
        repository.insert(item)
    }
    fun update(item: ShoppingItem){
        repository.update(item)
    }
    fun delete(item: ShoppingItem){
        repository.delete(item)
    }
}

class ShoppingListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingListViewModel(
                ShoppingListRepository.getInstance()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}