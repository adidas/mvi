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
    @Suppress("ktlint:standard:function-naming")
    public suspend fun WhenIntent(
        intent: TIntent,
        test: suspend ViewModelWhenContainerScope<TIntent, TState, TSideEffect, T>.() -> Unit,
    ) {
        registerContainer(
            name =
                TestName(
                    prefix = "When: ",
                    name = "${intent::class.simpleName} intent it's called",
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

    @Suppress("ktlint:standard:function-naming")
    public suspend fun When(
        name: String,
        test: suspend ViewModelWhenContainerScope<TIntent, TState, TSideEffect, T>.() -> Unit,
    ) {
        registerContainer(
            name = TestName(prefix = "When: ", name = name, defaultAffixes = true),
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
