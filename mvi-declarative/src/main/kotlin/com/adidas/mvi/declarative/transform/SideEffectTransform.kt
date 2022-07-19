package com.adidas.mvi.declarative.transform

import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.Transform

public abstract class SideEffectTransform<TState, TSideEffect> :
    Transform<MviState<TState, TSideEffect>> {

    protected abstract fun mutate(sideEffects: SideEffects<TSideEffect>): SideEffects<TSideEffect>

    final override fun reduce(currentState: MviState<TState, TSideEffect>): MviState<TState, TSideEffect> {
        val sideEffects = currentState.sideEffects
        return currentState.copy(sideEffects = mutate(sideEffects))
    }
}
