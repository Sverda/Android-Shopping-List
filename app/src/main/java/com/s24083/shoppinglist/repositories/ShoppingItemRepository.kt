package com.s24083.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.s24083.shoppinglist.entities.ShoppingItem
import androidx.lifecycle.MutableLiveData


class ShoppingItemRepository {
    private val list: MutableList<ShoppingItem> = mutableListOf()

    init {
        list.add(ShoppingItem(0, "Item 1", 11, 10.99, false))
        list.add(ShoppingItem(1, "Item 2", 22, 20.99, false))
        list.add(ShoppingItem(2, "Item 3", 33, 30.99, false))
    }

    val allItems: LiveData<List<ShoppingItem>>
        get() {
            val mList = MutableLiveData<List<ShoppingItem>>()
            mList.value = list
            return mList
        }

    fun insert(item: ShoppingItem) = list.add(item)
    fun update(item: ShoppingItem) {
        list.removeIf(){ value -> value.id == item.id}
        list.add(0, item)
    }
    fun delete(item: ShoppingItem) = list.removeIf(){ value -> value.id == item.id}
    fun deleteAll() = list.removeAll(list)
}