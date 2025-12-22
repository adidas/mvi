package com.adidas.mvi.kotest

import com.adidas.mvi.Intent
import com.adidas.mvi.LoggableState
import com.adidas.mvi.MviHost
import com.adidas.mvi.State
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.AbstractContainerScope
import io.kotest.core.test.TestScope

public class ViewModelContainerScope<
    TIntent : Intent,
    TState : LoggableState,
    TSideEffect : Any,
    T : MviHost<TIntent, State<TState, TSideEffect>>,
>(
    testScope: TestScope,
    private val viewModel: T,
) : AbstractContainerScope(testScope) {
    @Suppress("FunctionName")
    public suspend fun WhenIntent(
        intent: TIntent,
        test: suspend ViewModelWhenContainerScope<TIntent, TState, TSideEffect, T>.() -> Unit,
    ) {
        registerContainer(
            name =
                TestName(
                    name = "${intent::class.simpleName} intent is called",
                    focus = false,
                    bang = false,
                    prefix = "When: ",
                    suffix = null,
                    defaultAffixes = true,
                ),
            disabled = false,
            config = null,
        ) {
            this@ViewModelContainerScope.viewModel.execute(intent)
            ViewModelWhenContainerScope(
                testScope = this,
                viewModel = this@ViewModelContainerScope.viewModel,
            ).test()
        }
    }

    @Suppress("FunctionName")
    public suspend fun When(
        name: String,
        test: suspend ViewModelWhenContainerScope<TIntent, TState, TSideEffect, T>.() -> Unit,
    ) {
        registerContainer(
            name =
                TestName(
                    name = name,
                    focus = false,
                    bang = false,
                    prefix = "When: ",
                    suffix = null,
                    defaultAffixes = true,
                ),
            disabled = false,
            config = null,
        ) {
            ViewModelWhenContainerScope(
                this,
                viewModel = this@ViewModelContainerScope.viewModel,
            ).test()
        }
    }
}
