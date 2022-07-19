package com.adidas.mvi.declarative.transform

import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.transform.Transform

public abstract class StateTransform<TState, TSideEffect> : Transform<MviState<TState, TSideEffect>> {

    protected abstract fun mutate(currentState: TState): TState

    final override fun reduce(currentState: MviState<TState, TSideEffect>): MviState<TState, TSideEffect> {
        return currentState.copy(state = mutate(currentState.state))
    }
}
