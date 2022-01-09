package com.s24083.shoppinglist.ui.addStore

import android.content.Context
import androidx.lifecycle.*
import com.s24083.shoppinglist.data.StoresRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddStoreViewModel(
    private val repository: StoresRepository,
    private val storeId: Int?
)
    : ViewModel() {

    val name : MutableLiveData<String> = MediatorLiveData<String>()
    val description : MutableLiveData<String> = MediatorLiveData<String>()
    val radius : MutableLiveData<Int> = MediatorLiveData<Int>()
    val location : MutableLiveData<String> = MediatorLiveData<String>()

    init {
        viewModelScope.launch {
            load()
        }
    }

    private suspend fun load() = coroutineScope {
        println("in load with id of $storeId")
        location.value = "Loading..."
        storeId?.let { id ->
            val item = repository.getItem(id)
            println("item loaded ${item?.name}")
            name.value = item?.name
            description.value = item?.description
            radius.value = item?.radius
            location.value = item?.location
        }
    }
}

class AddStoreViewModelFactory(
    private val context: Context,
    private val storeId: Int?
)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddStoreViewModel(
                StoresRepository.getInstance(),
                storeId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}