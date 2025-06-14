package com.adidas.mvi.dsl

import com.adidas.mvi.Intent
import com.adidas.mvi.LoggableState
import com.adidas.mvi.MviHost
import com.adidas.mvi.State
import com.adidas.mvi.transform.StateTransform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public fun <ViewT : LoggableState, SideEffectT : Any, IntentT : Intent> MviHost<IntentT, State<ViewT, SideEffectT>>.intent(block: suspend IntentDslScope<ViewT, SideEffectT>.() -> Unit): Flow<StateTransform<State<ViewT, SideEffectT>>> =
    flow {
        val scope = IntentDslScope(this)
        scope.block()
    }
