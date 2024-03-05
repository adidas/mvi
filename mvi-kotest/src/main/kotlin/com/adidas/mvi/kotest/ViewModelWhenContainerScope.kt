package com.adidas.mvi.kotest

import com.adidas.mvi.Intent
import com.adidas.mvi.LoggableState
import com.adidas.mvi.MviHost
import com.adidas.mvi.State
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.AbstractContainerScope
import io.kotest.core.test.TestScope
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.StateFlow

public class ViewModelWhenContainerScope<
    TIntent : Intent,
    TState : LoggableState,
    TSideEffect : Any,
    T : MviHost<TIntent, State<TState, TSideEffect>>,
>(
    testScope: TestScope,
    public val viewModel: T,
) : AbstractContainerScope(testScope) {
    @Suppress("FunctionName")
    public suspend inline fun <reified TActualState : TState> ThenState(
        vararg sideEffects: TSideEffect,
        noinline test: suspend TestScope.(state: State<TActualState, TSideEffect>) -> Unit = {},
    ) {
        var name = "State should be [${TActualState::class.simpleName}]"
        if (sideEffects.isNotEmpty()) {
            name += " with sideEffect(s) ${sideEffects.map { it::class.java.simpleName }}"
        }

        registerTest(
            TestName("Then: ", name, true),
            false,
            null,
        ) {
            val state = (this@ViewModelWhenContainerScope.viewModel.state as StateFlow).value
            state.view.shouldBeInstanceOf<TActualState>()
            sideEffects.forEach {
                state.sideEffects.shouldContain(it)
            }

            test((this@ViewModelWhenContainerScope.viewModel.state as StateFlow).value as State<TActualState, TSideEffect>)
        }
    }

    @Suppress("FunctionName")
    public suspend fun AndIntent(
        intent: TIntent,
        test: suspend ViewModelWhenContainerScope<TIntent, TState, TSideEffect, T>.() -> Unit,
    ) {
        registerContainer(
            TestName("And: ", "${intent::class.simpleName} intent is executed", true),
            false,
            null,
        ) {
            this@ViewModelWhenContainerScope.viewModel.execute(intent)
            ViewModelWhenContainerScope(
                this,
                viewModel = this@ViewModelWhenContainerScope.viewModel,
            ).test()
        }
    }
}
