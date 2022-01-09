package com.s24083.shoppinglist.data.model

import kotlinx.serialization.Serializable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Serializable
data class LoggedInUser(
    val userId: String,
    val displayName: String
)