package com.adidas.mvi.reducer

import com.adidas.mvi.LoggableState

internal sealed class TestState : LoggableState {
    data object InitialState : TestState()

    data object StateFromTransform1 : TestState()

    data object AbelState : TestState()

    data object CainState : TestState()

    data object ImpossibleState : TestState()

    data object UniqueTransformState : TestState()
}
