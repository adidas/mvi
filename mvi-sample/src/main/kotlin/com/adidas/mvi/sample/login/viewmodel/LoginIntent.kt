package com.adidas.mvi.sample.login.viewmodel

import com.adidas.mvi.Intent

internal sealed class LoginIntent : Intent {

    data object Close : LoginIntent()

    data object Logout : LoginIntent()

    data class Login(val username: String, val password: String) : LoginIntent()
}