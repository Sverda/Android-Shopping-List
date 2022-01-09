package com.s24083.shoppinglist.ui.storesList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s24083.shoppinglist.data.StoresRepository
import com.s24083.shoppinglist.data.model.Store

class StoresListViewModel(private val repository: StoresRepository)
    : ViewModel() {
    val allItems: MutableLiveData<MutableList<Store>> = repository.getItems()

    fun insert(item: Store){
        repository.insert(item)
    }
    fun update(item: Store){
        repository.update(item)
    }
    fun delete(item: Store){
        repository.delete(item)
    }
}

class StoresListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoresListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoresListViewModel(
                StoresRepository.getInstance()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}