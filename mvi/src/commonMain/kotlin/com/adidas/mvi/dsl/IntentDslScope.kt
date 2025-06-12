package com.adidas.mvi.dsl

import com.adidas.mvi.LoggableState
import com.adidas.mvi.State
import com.adidas.mvi.requirements.requireAndReduceState
import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.SideEffectTransform
import com.adidas.mvi.transform.StateTransform
import com.adidas.mvi.transform.ViewTransform
import kotlinx.coroutines.flow.FlowCollector

public class IntentDslScope<StateT : LoggableState, SideEffectT : Any>(
    public val flowCollector: FlowCollector<StateTransform<State<StateT, SideEffectT>>>,
) {
    public suspend fun reduce(block: (StateT) -> StateT) {
        val transform =
            object : ViewTransform<StateT, SideEffectT>() {
                override fun mutate(currentState: StateT): StateT = block(currentState)
            }
        flowCollector.emit(transform)
    }

    public suspend fun postSideEffect(effect: SideEffectT) {
        val transform =
            object : SideEffectTransform<StateT, SideEffectT>() {
                override fun mutate(sideEffects: SideEffects<SideEffectT>): SideEffects<SideEffectT> = sideEffects.add(effect)
            }

        flowCollector.emit(transform)
    }

    public suspend inline fun <reified SpecificStateT : StateT> reduceInState(noinline reducer: (SpecificStateT) -> StateT) {
        val transform =
            object : ViewTransform<StateT, SideEffectT>() {
                override fun mutate(currentState: StateT): StateT {
                    return requireAndReduceState<StateT, SpecificStateT>(currentState) {
                        reducer(it)
                    }
                }
            }

        flowCollector.emit(transform)
    }
}
