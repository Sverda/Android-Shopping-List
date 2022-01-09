package com.s24083.shoppinglist.ui.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.data.LoginCache
import com.s24083.shoppinglist.data.LoginRepository
import com.s24083.shoppinglist.data.Result
import kotlinx.coroutines.coroutineScope

class LoginViewModel(application: Application, private val loginRepository: LoginRepository)
    : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val cache = LoginCache(context)

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    suspend fun login(username: String, password: String) = coroutineScope {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        if (result is Result.Success) {
            cache.Cache(result.data)
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = "login failed: $result")
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}