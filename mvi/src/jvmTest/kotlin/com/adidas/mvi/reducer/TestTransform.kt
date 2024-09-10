package com.adidas.mvi.reducer

import com.adidas.mvi.transform.ViewTransform

internal const val IMPOSSIBLE_INTENT_ID = 1
internal const val UNIQUE_INTENT_ID = 2

internal sealed class TestTransform : ViewTransform<TestState, TestSideEffect>() {
    data object Transform1 : TestTransform() {
        override fun mutate(currentState: TestState): TestState {
            return TestState.StateFromTransform1
        }
    }

    data object FailedTransform : TestTransform() {
        override fun mutate(currentState: TestState): TestState {
            throw Exception()
        }
    }

    data object AbelTransform : TestTransform() {
        override fun mutate(currentState: TestState): TestState {
            return TestState.AbelState
        }
    }

    data object CainTransform : TestTransform() {
        override fun mutate(currentState: TestState): TestState {
            return TestState.CainState
        }
    }

    data class UniqueTransform(val id: Int) : TestTransform() {
        override fun mutate(currentState: TestState): TestState {
            return when (id) {
                IMPOSSIBLE_INTENT_ID -> TestState.ImpossibleState
                UNIQUE_INTENT_ID -> TestState.UniqueTransformState
                else -> throw Throwable()
            }
        }
    }
}
