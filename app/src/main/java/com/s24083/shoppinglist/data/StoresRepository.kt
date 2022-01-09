package com.s24083.shoppinglist.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.s24083.shoppinglist.data.model.Store
import kotlinx.coroutines.tasks.await

class StoresRepository {
    companion object{
        private var instance: StoresRepository? = null

        fun getInstance(): StoresRepository {
            if (instance == null){
                instance = StoresRepository()
            }
            return instance as StoresRepository
        }
    }

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance("https://shoppinglist-40cf1-default-rtdb.europe-west1.firebasedatabase.app")
    private var dbRef: DatabaseReference = firebaseDatabase.reference.child("stores")

    suspend fun getItem(id: Int) : Store? {
        val itemSnapshot = dbRef.child(id.toString()).get().await()
        return itemSnapshot.getValue<Store>()
    }

    fun getItems(): MutableLiveData<MutableList<Store>>
    {
        val liveData = MutableLiveData<MutableList<Store>>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempList = mutableListOf<Store>()
                dataSnapshot?.children?.forEach{
                    tempList.add(it?.getValue<Store>() ?: Store())
                }
                liveData.postValue(tempList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        dbRef.addValueEventListener(listener)
        return liveData
    }

    fun insert(item: Store) = dbRef.child(item.id.toString()).setValue(item)
    fun update(item: Store) = dbRef.child(item.id.toString()).setValue(item)
    fun delete(item: Store) = dbRef.child(item.id.toString()).removeValue()
}