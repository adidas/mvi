package com.adidas.mvi.reducer

import com.adidas.mvi.State

internal sealed class TestState : State {
    object InitialState : TestState()

    object StateFromTransform1 : TestState()

    object AbelState : TestState()
    object CainState : TestState()

    object ImpossibleState : TestState()
    object UniqueTransformState : TestState()
}
