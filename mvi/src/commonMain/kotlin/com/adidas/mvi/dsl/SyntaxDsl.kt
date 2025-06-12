package com.adidas.mvi.dsl

import com.adidas.mvi.LoggableState
import com.adidas.mvi.State
import com.adidas.mvi.transform.StateTransform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public fun <StateT : LoggableState, SideEffectT : Any> intent(
    block: suspend IntentDslScope<StateT, SideEffectT>.() -> Unit,
): Flow<StateTransform<State<StateT, SideEffectT>>> = flow {
    val scope = IntentDslScope(this)
    scope.block()
}
