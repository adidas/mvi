package com.adidas.mvi.requirements

internal class DoubleReduceRequirement<TState>(
    private val leftReduceRequirement: ReduceRequirement<TState>,
    private val rightReduceRequirement: ReduceRequirement<TState>
) : ReduceRequirement<TState> {

    override fun reduce(state: TState): TState {
        return try {
            leftReduceRequirement.reduce(state)
        } catch (throwable: Throwable) {
            rightReduceRequirement.reduce(state)
        }
    }
}
