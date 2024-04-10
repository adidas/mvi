package com.adidas.mvi.sample.login.viewmodel

internal sealed class LoginSideEffect {

    data object ShowInvalidCredentialsError : LoginSideEffect()

    data object Close : LoginSideEffect()

}