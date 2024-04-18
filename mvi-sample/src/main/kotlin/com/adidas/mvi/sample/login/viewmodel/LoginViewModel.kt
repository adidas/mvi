package com.adidas.mvi.sample.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adidas.mvi.Logger
import com.adidas.mvi.MviHost
import com.adidas.mvi.State
import com.adidas.mvi.reducer.Reducer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

internal class LoginViewModel(
    logger: Logger,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel(), MviHost<LoginIntent, State<LoginState, LoginSideEffect>> {

    private val reducer =
        Reducer(
            coroutineScope = viewModelScope,
            defaultDispatcher = coroutineDispatcher,
            initialInnerState = LoginState.LoggedOut(isLoggingIn = false),
            logger = logger,
            intentExecutor = this::executeIntent,
        )

    override val state = reducer.state

    override fun execute(intent: LoginIntent) {
        reducer.executeIntent(intent)
    }

    private fun executeIntent(intent: LoginIntent) =
        when (intent) {
            is LoginIntent.Login -> executeLogin(intent)
            LoginIntent.Logout -> executeLogout()
            LoginIntent.Close -> executeClose()
        }

    private fun executeClose() = flow {
        emit(LoginTransform.AddSideEffect(LoginSideEffect.Close))
    }

    private fun executeLogout() = flow {
        emit(LoginTransform.SetShowLogin)
    }

    private fun executeLogin(intent: LoginIntent.Login) = flow {
        emit(LoginTransform.SetIsLoggingIn(isLoggingIn = true))

        delay(300)

        emit(LoginTransform.SetIsLoggingIn(isLoggingIn = false))

        if (intent.username.isEmpty() || intent.password.isEmpty()) {
            emit(LoginTransform.AddSideEffect(LoginSideEffect.ShowInvalidCredentialsError))
        } else {
            emit(LoginTransform.SetLoggedIn(intent.username))
        }
    }
}