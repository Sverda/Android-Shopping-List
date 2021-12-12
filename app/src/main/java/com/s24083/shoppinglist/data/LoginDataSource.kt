package com.s24083.shoppinglist.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.s24083.shoppinglist.data.model.LoggedInUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private var auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String) : Result<LoggedInUser> {
        try {
            println("start signing action")
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                return Result.Success(LoggedInUser(user.uid, user.displayName ?: ""))
            }

            println("there is no current user")
            try {
                val loginResult = auth.signInWithEmailAndPassword(email, password).await()
                loginResult.user?.let { user ->
                    return Result.Success(LoggedInUser(user.uid, user.displayName ?: ""))
                }
            } catch (e: Throwable) {
                if (e is FirebaseAuthException) {
                    if (e.errorCode != "ERROR_USER_NOT_FOUND") {
                        Log.e("LoginDataSource", "Error while signing", e)
                        return Result.Error(IOException("Error logging in", e))
                    }
                } else {
                    Log.e("LoginDataSource", "Error while signing", e)
                    return Result.Error(IOException("Error logging in", e))
                }
            }

            println("signing not successful")
            val registerResult = auth.createUserWithEmailAndPassword(email, password).await()
            registerResult.user?.let { user ->
                return Result.Success(LoggedInUser(user.uid, user.displayName ?: ""))
            }

            return Result.Error(Exception("Error logging in"))
        } catch (e: Throwable) {
            Log.e("LoginDataSource", "Error while authenticating", e)
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        auth.signOut()
    }
}