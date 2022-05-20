package com.adidas.mvi.requirements

public interface ReduceRequirement<TState> {
    public fun reduce(state: TState): TState
}
