package com.adidas.mvi.declarative.transform

import com.adidas.mvi.actions.Actions
import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.transform.Transform

public abstract class ActionTransform<TState, TAction> :
    Transform<MviState<TState, TAction>> {

    protected abstract fun mutate(actions: Actions<TAction>): Actions<TAction>

    final override fun reduce(currentState: MviState<TState, TAction>): MviState<TState, TAction> {
        val actions = currentState.actions
        return currentState.copy(actions = mutate(actions))
    }
}
