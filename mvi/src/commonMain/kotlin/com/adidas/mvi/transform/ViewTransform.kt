package com.adidas.mvi.transform

import com.adidas.mvi.State

public abstract class ViewTransform<TView, TSideEffect> : StateTransform<State<TView, TSideEffect>> {
    protected abstract fun mutate(currentState: TView): TView

    final override fun reduce(currentState: State<TView, TSideEffect>): State<TView, TSideEffect> {
        return currentState.copy(view = mutate(currentState.view))
    }
}
