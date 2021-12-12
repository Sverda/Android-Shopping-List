package com.s24083.shoppinglist.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.s24083.shoppinglist.data.model.ShoppingItem
import kotlinx.coroutines.tasks.await


class ShoppingListRepository {
    companion object{
        private var instance: ShoppingListRepository? = null

        fun getInstance(): ShoppingListRepository {
            if (instance == null){
                instance = ShoppingListRepository()
            }
            return instance as ShoppingListRepository
        }
    }

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance("https://shoppinglist-40cf1-default-rtdb.europe-west1.firebasedatabase.app")
    private var dbRef: DatabaseReference = firebaseDatabase.reference.child("shoppingItems")

    suspend fun getItem(id: Int) : ShoppingItem? {
        val itemSnapshot = dbRef.child(id.toString()).get().await()
        return itemSnapshot.getValue<ShoppingItem>()
    }

    fun getItems(): MutableLiveData<MutableList<ShoppingItem>>
    {
        val liveData = MutableLiveData<MutableList<ShoppingItem>>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempList = mutableListOf<ShoppingItem>()
                dataSnapshot?.children?.forEach{
                    tempList.add(it?.getValue<ShoppingItem>() ?: ShoppingItem())
                }
                liveData.postValue(tempList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        dbRef.addValueEventListener(listener)
        return liveData
    }

    fun insert(item: ShoppingItem) = dbRef.child(item.id.toString()).setValue(item)
    fun update(item: ShoppingItem) = dbRef.child(item.id.toString()).setValue(item)
    fun delete(item: ShoppingItem) = dbRef.child(item.id.toString()).removeValue()
}