package com.s24083.shoppinglist.repositories

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.s24083.shoppinglist.entities.ShoppingItem


class ShoppingListFirebaseRepository {
    private val list: MutableList<ShoppingItem> = mutableListOf()
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance("https://shoppinglist-40cf1-default-rtdb.europe-west1.firebasedatabase.app")
    private var dbRef: DatabaseReference = firebaseDatabase.reference.child("shoppingItems")

    init {
        dbRef.child("shoppingItems").get().addOnSuccessListener  {
            val item = it.getValue<ShoppingItem>()
            if (item != null) {
                list.add(item)
            }
        }
    }

    val allItems: MutableList<ShoppingItem>
        get() {
            return list
        }

    fun insert(item: ShoppingItem) = dbRef.child(item.id.toString()).setValue(item)
    fun update(modifiedItem: ShoppingItem) {
        val currentItem = list.first { value -> value.id == modifiedItem.id }
        currentItem.update(modifiedItem)
    }
    fun delete(item: ShoppingItem) = list.removeIf(){ value -> value.id == item.id}
    fun deleteAll() = list.removeAll(list)
}