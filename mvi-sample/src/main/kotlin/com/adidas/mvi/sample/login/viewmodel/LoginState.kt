package com.adidas.mvi.sample.login.viewmodel

import com.adidas.mvi.LoggableState

public sealed class LoginState : LoggableState {

    public data class LoggedOut(val isLoggingIn: Boolean) : LoginState()

    public data class LoggedIn(val username: String) : LoginState()
}