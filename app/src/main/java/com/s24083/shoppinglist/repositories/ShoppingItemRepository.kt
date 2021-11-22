package com.s24083.shoppinglist.repositories

import com.s24083.shoppinglist.entities.ShoppingItem


object ShoppingItemRepository {
    private val list: MutableList<ShoppingItem> = mutableListOf()

    init {
        list.add(ShoppingItem(0, "Item 1", 11, 10.99, false))
        list.add(ShoppingItem(1, "Item 2", 22, 20.99, false))
        list.add(ShoppingItem(2, "Item 3", 33, 30.99, true))
    }

    val allItems: MutableList<ShoppingItem>
        get() {
            return list
        }

    fun insert(item: ShoppingItem) = list.add(item)
    fun update(modifiedItem: ShoppingItem) {
        val currentItem = list.first { value -> value.id == modifiedItem.id }
        currentItem.update(modifiedItem)
    }
    fun delete(item: ShoppingItem) = list.removeIf(){ value -> value.id == item.id}
    fun deleteAll() = list.removeAll(list)
}