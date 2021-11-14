package com.s24083.shoppinglist.ShoppingList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s24083.shoppinglist.entities.ShoppingItem
import com.s24083.shoppinglist.repositories.ShoppingItemRepository

class ShoppingListViewModel(private val repository: ShoppingItemRepository)
    : ViewModel() {
    val allItems: MutableLiveData<MutableList<ShoppingItem>> = MutableLiveData(repository.allItems)

    fun insert(item: ShoppingItem){
        repository.insert(item)
        allItems.postValue(repository.allItems)
    }
    fun update(item: ShoppingItem){
        repository.update(item)
        allItems.postValue(repository.allItems)
    }
    fun delete(item: ShoppingItem){
        repository.delete(item)
        allItems.postValue(repository.allItems)
    }
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