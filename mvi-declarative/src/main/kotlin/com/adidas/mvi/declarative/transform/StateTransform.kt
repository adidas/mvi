package com.adidas.mvi.declarative.transform

import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.transform.Transform

public abstract class StateTransform<TState, TAction> : Transform<MviState<TState, TAction>> {

    protected abstract fun mutate(currentState: TState): TState

    final override fun reduce(currentState: MviState<TState, TAction>): MviState<TState, TAction> {
        return currentState.copy(state = mutate(currentState.state))
    }
}
