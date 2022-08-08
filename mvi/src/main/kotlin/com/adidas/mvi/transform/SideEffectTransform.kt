package com.adidas.mvi.transform

import com.adidas.mvi.State
import com.adidas.mvi.sideeffects.SideEffects

public abstract class SideEffectTransform<TView, TSideEffect> :
    StateTransform<State<TView, TSideEffect>> {

    protected abstract fun mutate(sideEffects: SideEffects<TSideEffect>): SideEffects<TSideEffect>

    final override fun reduce(currentState: State<TView, TSideEffect>): State<TView, TSideEffect> {
        val sideEffects = currentState.sideEffects
        return currentState.copy(sideEffects = mutate(sideEffects))
    }
}
