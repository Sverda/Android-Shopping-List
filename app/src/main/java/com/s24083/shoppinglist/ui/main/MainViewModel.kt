package com.s24083.shoppinglist.ui.main

import androidx.lifecycle.ViewModel
import com.s24083.shoppinglist.data.LoginRepository

class MainViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun logout(){
        loginRepository.logout()
    }
}