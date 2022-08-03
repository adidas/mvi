package com.adidas.mvi.transform

import com.adidas.mvi.MviState
import com.adidas.mvi.sideeffects.SideEffects

public abstract class SideEffectTransform<TState, TSideEffect> :
    StateTransform<MviState<TState, TSideEffect>> {

    protected abstract fun mutate(sideEffects: SideEffects<TSideEffect>): SideEffects<TSideEffect>

    final override fun reduce(currentState: MviState<TState, TSideEffect>): MviState<TState, TSideEffect> {
        val sideEffects = currentState.sideEffects
        return currentState.copy(sideEffects = mutate(sideEffects))
    }
}
