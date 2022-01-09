package com.s24083.shoppinglist.data

import android.content.Context
import com.s24083.shoppinglist.data.model.LoggedInUser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LoginCache(private val context: Context) {
    private val filename = "loggedIn"

    fun Cache(user: LoggedInUser) {
        val content = Json.encodeToString(user)
        File.createTempFile(filename, null, context.cacheDir)
        val cacheFile = File(context.cacheDir, filename)
        cacheFile.writeText(content)
    }

    fun Retrive() : LoggedInUser? {
        val cacheFile = File(context.cacheDir, filename)
        if (!cacheFile.exists()) {
            return null
        }

        val content = cacheFile.readText()
        return Json.decodeFromString<LoggedInUser>(content)
    }

    fun Clean() {
        val cacheFile = File(context.cacheDir, filename)
        cacheFile.delete()
    }
}