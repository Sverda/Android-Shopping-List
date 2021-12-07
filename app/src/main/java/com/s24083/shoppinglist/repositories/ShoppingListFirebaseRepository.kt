package com.s24083.shoppinglist.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.s24083.shoppinglist.entities.ShoppingItem


class ShoppingListFirebaseRepository {
    private val list: MutableList<ShoppingItem> = mutableListOf()
    private var dbRef: DatabaseReference = Firebase.database.getReference("shoppingItems")

    init {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<ShoppingItem>() ?: return
                list.add(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
        // Read from the database
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<ShoppingItem>()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
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