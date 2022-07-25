package com.adidas.mvi.declarative

import com.adidas.mvi.State
import com.adidas.mvi.sideeffects.SideEffects

public data class MviState<out TState, TSideEffect>(val state: TState, val sideEffects: SideEffects<TSideEffect>) :
    State {

    public abstract class StateTransform<TState, TSideEffect> :
        com.adidas.mvi.transform.StateTransform<MviState<TState, TSideEffect>> {

        protected abstract fun mutate(currentState: TState): TState

        final override fun reduce(currentState: MviState<TState, TSideEffect>): MviState<TState, TSideEffect> {
            return currentState.copy(state = mutate(currentState.state))
        }
    }

    public abstract class SideEffectTransform<TState, TSideEffect> :
        com.adidas.mvi.transform.StateTransform<MviState<TState, TSideEffect>> {

        protected abstract fun mutate(sideEffects: SideEffects<TSideEffect>): SideEffects<TSideEffect>

        final override fun reduce(currentState: MviState<TState, TSideEffect>): MviState<TState, TSideEffect> {
            val sideEffects = currentState.sideEffects
            return currentState.copy(sideEffects = mutate(sideEffects))
        }
    }
}
