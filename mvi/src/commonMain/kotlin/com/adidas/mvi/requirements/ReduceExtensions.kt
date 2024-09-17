package com.adidas.mvi.requirements

import com.adidas.mvi.LoggableState

public inline fun <TState : LoggableState, reified TRequiredState : TState> requireAndReduceState(
    state: TState,
    noinline reduce: (TRequiredState) -> TState,
): TState {
    return StateReduceRequirement(TRequiredState::class, reduce).reduce(state)
}

public inline fun <TState : LoggableState, reified TRequiredState : TState> requireState(noinline reduce: (TRequiredState) -> TState): ReduceRequirement<TState> {
    return StateReduceRequirement(TRequiredState::class, reduce)
}

public infix fun <TState : LoggableState> ReduceRequirement<TState>.or(another: ReduceRequirement<TState>): ReduceRequirement<TState> {
    return DoubleReduceRequirement(this, another)
}
