package com.adidas.mvi.declarative

import com.adidas.mvi.Intent
import com.adidas.mvi.LoggableState
import com.adidas.mvi.Logger
import com.adidas.mvi.Reducer
import com.adidas.mvi.SimplifiedIntentExecutor
import com.adidas.mvi.sideeffects.SideEffects
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

public fun <TIntent : Intent, TInnerState : LoggableState, TAction> Reducer(
    coroutineScope: CoroutineScope,
    initialInnerState: TInnerState,
    intentExecutor: SimplifiedIntentExecutor<TIntent, MviState<TInnerState, TAction>>,
    logger: Logger? = null,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): Reducer<TIntent, MviState<TInnerState, TAction>> {
    return Reducer(
        coroutineScope = coroutineScope,
        initialState = MviState(initialInnerState, SideEffects()),
        intentExecutor = intentExecutor,
        logger = logger,
        defaultDispatcher = defaultDispatcher
    )
}
