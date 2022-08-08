package com.adidas.mvi.reducer

import com.adidas.mvi.Intent
import com.adidas.mvi.LoggableState
import com.adidas.mvi.Logger
import com.adidas.mvi.Reducer
import com.adidas.mvi.SimplifiedIntentExecutor
import com.adidas.mvi.State
import com.adidas.mvi.sideeffects.SideEffects
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

public fun <TIntent : Intent, TInnerState : LoggableState, TAction> Reducer(
    coroutineScope: CoroutineScope,
    initialInnerState: TInnerState,
    intentExecutor: SimplifiedIntentExecutor<TIntent, State<TInnerState, TAction>>,
    logger: Logger? = null,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): Reducer<TIntent, State<TInnerState, TAction>> {
    return Reducer(
        coroutineScope = coroutineScope,
        initialState = State(initialInnerState, SideEffects()),
        intentExecutor = intentExecutor,
        logger = logger,
        defaultDispatcher = defaultDispatcher
    )
}

@Suppress("UNCHECKED_CAST")
public inline fun <reified TView : Any> Reducer<*, *>.requireView(): TView =
    (state.value as State<TView, *>).view.apply {
        if (this.javaClass != TView::class.java) {
            throw ClassCastException("Required view of ${TView::class.java} type, but found ${this.javaClass}")
        }
    }
