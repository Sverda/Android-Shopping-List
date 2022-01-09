package com.s24083.shoppinglist.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Store(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val radius: Int = 0
)