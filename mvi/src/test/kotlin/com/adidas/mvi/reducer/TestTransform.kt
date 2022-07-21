package com.adidas.mvi.reducer

import com.adidas.mvi.transform.StateTransform

internal const val IMPOSSIBLE_INTENT_ID = 1
internal const val UNIQUE_INTENT_ID = 2

internal sealed class TestTransform : StateTransform<TestState> {
    object Transform1 : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            return TestState.StateFromTransform1
        }
    }

    object FailedTransform : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            throw Exception()
        }
    }

    object AbelTransform : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            return TestState.AbelState
        }
    }

    object CainTransform : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            return TestState.CainState
        }
    }

    data class UniqueTransform(val id: Int) : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            return when (id) {
                IMPOSSIBLE_INTENT_ID -> TestState.ImpossibleState
                UNIQUE_INTENT_ID -> TestState.UniqueTransformState
                else -> throw Throwable()
            }
        }
    }
}
