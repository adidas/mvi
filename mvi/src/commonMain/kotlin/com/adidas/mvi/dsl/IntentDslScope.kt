package com.adidas.mvi.dsl

import com.adidas.mvi.LoggableState
import com.adidas.mvi.State
import com.adidas.mvi.requirements.requireAndReduceState
import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.SideEffectTransform
import com.adidas.mvi.transform.StateTransform
import com.adidas.mvi.transform.ViewTransform
import kotlinx.coroutines.flow.FlowCollector

public class IntentDslScope<ViewT : LoggableState, SideEffectT : Any>(
    public val flowCollector: FlowCollector<StateTransform<State<ViewT, SideEffectT>>>,
) {
    public suspend fun reduce(block: (ViewT) -> ViewT) {
        val transform =
            object : ViewTransform<ViewT, SideEffectT>() {
                override fun mutate(currentState: ViewT): ViewT = block(currentState)
            }
        flowCollector.emit(transform)
    }

    public suspend fun postSideEffect(effect: SideEffectT) {
        val transform =
            object : SideEffectTransform<ViewT, SideEffectT>() {
                override fun mutate(sideEffects: SideEffects<SideEffectT>): SideEffects<SideEffectT> = sideEffects.add(effect)
            }

        flowCollector.emit(transform)
    }

    public suspend inline fun <reified SpecificViewT : ViewT> reduceInState(noinline reducer: (SpecificViewT) -> ViewT) {
        val transform =
            object : ViewTransform<ViewT, SideEffectT>() {
                override fun mutate(currentState: ViewT): ViewT {
                    return requireAndReduceState<ViewT, SpecificViewT>(currentState) {
                        reducer(it)
                    }
                }
            }

        flowCollector.emit(transform)
    }
}
