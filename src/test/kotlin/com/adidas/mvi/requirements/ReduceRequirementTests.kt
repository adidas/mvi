package com.adidas.mvi.requirements

import com.adidas.mvi.State
import com.adidas.mvi.StateRequiredNotFulfilledException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

private sealed class TestState : State {
    object State1 : TestState()
    object State2 : TestState()
    object State3 : TestState()
}

internal class ReduceRequirementTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    given("A state requirement") {

        val stateReduceRequirement = StateReduceRequirement(TestState.State1::class) {
            TestState.State2
        }

        `when`("I try to convert using the required state") {
            val newState = stateReduceRequirement.reduce(TestState.State1)

            then("It should convert to the new state") {
                newState.shouldBeInstanceOf<TestState.State2>()
            }
        }

        `when`("I try to convert using the wrong state") {
            then("It should throw an exception") {
                shouldThrow<StateRequiredNotFulfilledException> {
                    stateReduceRequirement.reduce(TestState.State2)
                }
            }
        }
    }

    given("Two Requirements composed by a DoubleReduceRequirement") {
        val leftRequirement = mockk<ReduceRequirement<TestState>>()
        val rightRequirement = mockk<ReduceRequirement<TestState>>()
        val doubleReduceRequirement = DoubleReduceRequirement(leftRequirement, rightRequirement)

        beforeContainer {
            clearAllMocks()
        }

        `when`("The left requirement is successful and the right fails") {
            every { leftRequirement.reduce(TestState.State1) } returns TestState.State2
            every { rightRequirement.reduce(TestState.State1) } throws Exception()
            val newState = doubleReduceRequirement.reduce(TestState.State1)

            then("The state should be transformed by the left") {
                newState shouldBe TestState.State2
            }
        }

        `when`("The left requirement fails and the right is successful") {
            every { leftRequirement.reduce(TestState.State1) } throws Exception()
            every { rightRequirement.reduce(TestState.State1) } returns TestState.State2
            val newState = doubleReduceRequirement.reduce(TestState.State1)

            then("The state should be transformed by the right") {
                newState shouldBe TestState.State2
            }
        }

        `when`("Both requirements fail") {
            every { leftRequirement.reduce(TestState.State1) } throws Exception()
            every { rightRequirement.reduce(TestState.State1) } throws ClassCastException()

            then("The exception should be thrown by the right requirement") {
                shouldThrow<ClassCastException> {
                    doubleReduceRequirement.reduce(TestState.State1)
                }
            }
        }
    }

    given("The or extension and two requirements") {
        val leftRequirement = mockk<ReduceRequirement<TestState>>()
        val rightRequirement = mockk<ReduceRequirement<TestState>>()

        beforeContainer {
            clearAllMocks()
        }

        `when`("The left requirement is successful and the right fails") {
            every { leftRequirement.reduce(TestState.State1) } returns TestState.State2
            every { rightRequirement.reduce(TestState.State1) } throws Exception()
            val newState = (leftRequirement or rightRequirement).reduce(TestState.State1)

            then("The state should be transformed by the left") {
                newState shouldBe TestState.State2
            }
        }
    }

    given("The or extension and two requirements produced by the requireState extension") {
        val reduceRequirement = requireState<TestState, TestState.State1> { TestState.State2 } or requireState<TestState, TestState.State2> { TestState.State3 }

        `when`("I try to use with a state required by the right requirement") {
            val newState = reduceRequirement.reduce(TestState.State2)

            then("The state should be transformed by the right requirement") {
                newState shouldBe TestState.State3
            }
        }
    }

    given("The or extension and three requirements produced by the requireState extension") {
        val reduceRequirement = requireState<TestState, TestState.State1> {
            TestState.State3
        } or requireState<TestState, TestState.State2> {
            TestState.State1
        } or requireState<TestState, TestState.State3> {
            TestState.State2
        }

        `when`("I try to use with a state required by the last, third requirement") {
            val newState = reduceRequirement.reduce(TestState.State3)

            then("The state should be transformed by the last, third requirement") {
                newState shouldBe TestState.State2
            }
        }
    }

    given("The requireAndReduceState") {
        `when`("I try to use it with a correct state") {
            val newState = requireAndReduceState(TestState.State1 as TestState) {
                TestState.State2
            }

            then("The state should be transformed") {
                newState shouldBe TestState.State2
            }
        }
    }
})
