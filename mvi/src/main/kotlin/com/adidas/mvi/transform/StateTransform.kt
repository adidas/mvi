package com.adidas.mvi.transform

import com.adidas.mvi.Loggable
import kotlinx.coroutines.CoroutineDispatcher

public interface StateTransform<TState> : Loggable {
    public suspend fun reduce(
        currentState: TState,
        defaultDispatcher: CoroutineDispatcher
    ): TState {
        return this.reduce(currentState)
    }

    public fun reduce(currentState: TState): TState
}
