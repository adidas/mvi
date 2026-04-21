package com.adidas.mvi.reducer

import com.adidas.mvi.Intent
import com.adidas.mvi.IntentExecutor
import com.adidas.mvi.LoggableState
import com.adidas.mvi.Logger
import com.adidas.mvi.Reducer
import com.adidas.mvi.State
import com.adidas.mvi.sideeffects.SideEffects
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

public fun <TIntent : Intent, TInnerState : LoggableState, TAction> Reducer(
    coroutineScope: CoroutineScope,
    initialInnerState: TInnerState,
    intentExecutor: IntentExecutor<TIntent, State<TInnerState, TAction>>,
    logger: Logger? = null,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
): Reducer<TIntent, State<TInnerState, TAction>> {
    return Reducer(
        coroutineScope = coroutineScope,
        initialState = State(initialInnerState, SideEffects()),
        intentExecutor = intentExecutor,
        logger = logger,
        defaultDispatcher = defaultDispatcher,
    )
}

@Suppress("UNCHECKED_CAST")
public inline fun <reified TView : Any> Reducer<*, *>.requireView(): TView =
    (state.value as State<TView, *>).view.apply {
        if (!TView::class.isInstance(this)) {
            throw ClassCastException("Required view of ${TView::class} type, but found $this")
        }
    }

/**
 * Wait until reducer's view is [T] and then return that View. Note that this function can potentially suspend
 * indefinitely if the view is never reached.
 *
 * To use, you have to specify two parameters, first one is the target state, the second one can be left blank. Like this:
 * `val state = reducer.awaitView<MyLoadedState, _>()`
 */
public suspend inline fun <reified T : P, P> Reducer<*, out State<P, *>>.awaitView(): T {
    return state.map { it.view }.filterIsInstance<T>().first()
}
