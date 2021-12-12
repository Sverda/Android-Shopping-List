package com.s24083.shoppinglist.addShoppingItem

import android.content.Context
import androidx.lifecycle.*
import com.s24083.shoppinglist.repositories.ShoppingListRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddShoppingItemViewModel(
    private val repository: ShoppingListRepository,
    private val shoppingItemId: Int?
)
    : ViewModel() {

    val name : MutableLiveData<String> = MediatorLiveData<String>()
    val price : MutableLiveData<Double> = MediatorLiveData<Double>()
    val amount : MutableLiveData<Int> = MediatorLiveData<Int>()

    init {
        viewModelScope.launch {
            load()
        }
    }

    private suspend fun load() = coroutineScope {
        println("in load with id of $shoppingItemId")
        shoppingItemId?.let { id ->
            val item = repository.getItem(id)
            println("item loaded ${item?.name}")
            name.value = item?.name
            price.value = item?.price
            amount.value = item?.amount
        }
    }
}

class AddShoppingItemViewModelFactory(
    private val context: Context,
    private val shoppingItemId: Int?
)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddShoppingItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddShoppingItemViewModel(
                ShoppingListRepository.getInstance(),
                shoppingItemId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}