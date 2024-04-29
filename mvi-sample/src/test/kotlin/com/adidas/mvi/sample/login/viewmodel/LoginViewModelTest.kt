package com.adidas.mvi.sample.login.viewmodel

import com.adidas.mvi.kotest.GivenViewModel
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val testCoroutineDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    listeners(CoroutineListener(testCoroutineDispatcher))

    fun getViewModel(): LoginViewModel {
        return LoginViewModel(
            logger = mockk(relaxed = true),
            coroutineDispatcher = testCoroutineDispatcher
        )
    }

    GivenViewModel(::getViewModel) {
        When("no intent is executed") {
            ThenState<LoginState.LoggedOut>()
        }

        WhenIntent(LoginIntent.Close) {
            ThenState<LoginState.LoggedOut>(LoginSideEffect.Close)
        }

        WhenIntent(LoginIntent.Login("username", "password")) {

            ThenState<LoginState.LoggedOut> { state ->
                state.view.isLoggingIn shouldBe true
            }

            testCoroutineDispatcher.scheduler.advanceUntilIdle()

            ThenState<LoginState.LoggedIn>()
        }

        WhenIntent(LoginIntent.Logout) {
            ThenState<LoginState.LoggedOut>()
        }

        WhenIntent(LoginIntent.Login("", "")) {
            testCoroutineDispatcher.scheduler.advanceUntilIdle()

            ThenState<LoginState.LoggedOut>(LoginSideEffect.ShowInvalidCredentialsError)
        }

    }

})