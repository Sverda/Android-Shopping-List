package com.s24083.shoppinglist.entities

class ShoppingItem(val id: Int,
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