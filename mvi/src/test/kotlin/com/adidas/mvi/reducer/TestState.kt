package com.adidas.mvi.reducer

import com.adidas.mvi.LoggableState

internal sealed class TestState : LoggableState {
    object InitialState : TestState()

    object StateFromTransform1 : TestState()

    object AbelState : TestState()
    object CainState : TestState()

    object ImpossibleState : TestState()
    object UniqueTransformState : TestState()
}
