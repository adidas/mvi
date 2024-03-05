package com.adidas.mvi.kotest

import com.adidas.mvi.Intent
import com.adidas.mvi.LoggableState
import com.adidas.mvi.MviHost
import com.adidas.mvi.State
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.BehaviorSpecRootScope
import io.kotest.core.spec.style.scopes.addContainer

@Suppress("ktlint:standard:function-naming")
public inline fun <
    TIntent : Intent,
    TState : LoggableState,
    TSideEffect : Any,
    reified T : MviHost<TIntent, State<TState, TSideEffect>>,
> BehaviorSpecRootScope.GivenViewModel(
    noinline viewModel: () -> T,
    crossinline test: suspend ViewModelContainerScope<TIntent, TState, TSideEffect, T>.() -> Unit,
): Unit =
    addContainer(
        TestName("Given: ", "a ${T::class.simpleName}", true),
        disabled = false,
        null,
    ) {
        ViewModelContainerScope(this, viewModel()).test()
    }
