package com.adidas.mvi.sample.login.viewmodel

import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.SideEffectTransform
import com.adidas.mvi.transform.ViewTransform

internal object LoginTransform {

    object SetShowLogin : ViewTransform<LoginState, LoginSideEffect>() {
        override fun mutate(currentState: LoginState): LoginState {
            return LoginState.LoggedOut(isLoggingIn = false)
        }
    }

    data class SetIsLoggingIn(val isLoggingIn: Boolean) :
        ViewTransform<LoginState, LoginSideEffect>() {
        override fun mutate(currentState: LoginState): LoginState {
            return LoginState.LoggedOut(isLoggingIn = isLoggingIn)
        }
    }

    data class SetLoggedIn(val username: String) : ViewTransform<LoginState, LoginSideEffect>() {
        override fun mutate(currentState: LoginState): LoginState {
            return LoginState.LoggedIn(username)
        }
    }

    data class AddSideEffect(
        val sideEffect: LoginSideEffect,
    ) : SideEffectTransform<LoginState, LoginSideEffect>() {
        override fun mutate(sideEffects: SideEffects<LoginSideEffect>): SideEffects<LoginSideEffect> {
            return sideEffects.add(sideEffect)
        }
    }
}