package com.adidas.mvi.transform

import com.adidas.mvi.State
import com.adidas.mvi.sideeffects.SideEffects

public abstract class SideEffectTransform<TState, TSideEffect> :
    StateTransform<State<TState, TSideEffect>> {

    protected abstract fun mutate(sideEffects: SideEffects<TSideEffect>): SideEffects<TSideEffect>

    final override fun reduce(currentState: State<TState, TSideEffect>): State<TState, TSideEffect> {
        val sideEffects = currentState.sideEffects
        return currentState.copy(sideEffects = mutate(sideEffects))
    }
}
