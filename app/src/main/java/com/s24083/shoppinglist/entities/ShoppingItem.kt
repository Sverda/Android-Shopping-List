package com.s24083.shoppinglist.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingItem(val id: Int,
                   var name: String,
                   var amount: Int,
                   var price: Double,
                   var isBought: Boolean) {

    fun update(item: ShoppingItem) {
        name = item.name
        amount = item.amount
        price = item.price
        isBought = item.isBought
    }
}