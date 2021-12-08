package com.s24083.shoppinglist.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingItem(val id: Int = 0,
                   var name: String = "",
                   var amount: Int = 0,
                   var price: Double = 0.0,
                   var isBought: Boolean = false) {

    fun update(item: ShoppingItem) {
        name = item.name
        amount = item.amount
        price = item.price
        isBought = item.isBought
    }
}