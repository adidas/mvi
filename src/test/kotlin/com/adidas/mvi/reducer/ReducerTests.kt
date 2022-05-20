package com.adidas.mvi.reducer

import com.adidas.mvi.CoroutineListener
import com.adidas.mvi.IntentExecutor
import com.adidas.mvi.Logger
import com.adidas.mvi.Reducer
import com.adidas.mvi.TerminatedIntentException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope

internal class ReducerTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val intentExecutor = mockk<IntentExecutor<TestIntent, TestTransform>>()
    val logger = mockk<Logger>(relaxUnitFun = true)

    val coroutineListener = CoroutineListener()
    listeners(coroutineListener)

    fun createReducer() = Reducer(
        coroutineScope = TestScope(coroutineListener.testCoroutineDispatcher),
        initialState = TestState.InitialState,
        defaultDispatcher = coroutineListener.testCoroutineDispatcher,
        logger = logger,
        intentExecutor = intentExecutor
    )

    Given("A reducer with a initial state") {
        val reducer = createReducer()

        When("I listen to its first value") {
            val firstValue = reducer.state.value

            Then("It should be the initial state") {
                firstValue shouldBe TestState.InitialState
            }
        }

        When("I try to require a state different than the current state") {
            Then("It should cause a ClassCastException") {
                shouldThrow<ClassCastException> {
                    reducer.requireState<TestState.StateFromTransform1>()
                }
            }
        }

        When("I try to require a state equal to the current state") {
            val requiredState = reducer.requireState<TestState.InitialState>()

            Then("It should return the cast state") {
                requiredState shouldBe TestState.InitialState
            }
        }

        When("I execute an intent") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            Then("This intent should be logged") {
                verify(exactly = 1) { logger.logIntent(TestIntent.SimpleIntent) }
                verify(exactly = 1) { intentExecutor.executeIntent(TestIntent.SimpleIntent, any()) }
            }
        }
    }

    Given("A reducer which triggers a TerminatedIntentException when executing a SimpleIntent") {
        val exceptionToThrow = TerminatedIntentException()
        every { intentExecutor.executeIntent(TestIntent.SimpleIntent, any()) } throws exceptionToThrow
        val reducer = createReducer()

        When("I execute an intent and it is cancelled throwing TerminatedIntentException") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            Then("The exception should NOT be logged") {
                verify(exactly = 0) { logger.logFailedIntent(TestIntent.SimpleIntent, exceptionToThrow) }
            }
        }
    }

    Given("A reducer which fails to execute a SimpleIntent") {
        val reducer = createReducer()

        val exceptionToThrow = Exception()
        every { intentExecutor.executeIntent(TestIntent.SimpleIntent, any()) } throws exceptionToThrow

        When("I execute an intent") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            Then("The exception should be logged") {
                verify(exactly = 1) { logger.logFailedIntent(TestIntent.SimpleIntent, exceptionToThrow) }
            }
        }
    }

    Given("A reducer which produces Transform1 partial state when Transform1Producer intent is sent") {
        val reducer = createReducer()

        every {
            intentExecutor.executeIntent(
                TestIntent.Transform1Producer,
                any()
            )
        } returns flowOf(TestTransform.Transform1)

        When("I execute Transform1Producer intent") {
            reducer.executeIntent(TestIntent.Transform1Producer)

            Then("The state should change to the state which Transform1 produces") {
                reducer.state.value shouldBe TestState.StateFromTransform1
                verify(exactly = 1) {
                    logger.logTransformedNewState(
                        transform = TestTransform.Transform1,
                        previousState = TestState.InitialState,
                        newState = TestState.StateFromTransform1
                    )
                }
            }
        }
    }

    Given("A reducer which produces FailedTransform partial state when FailedTransformProducer intent is sent") {
        val reducer = createReducer()

        every {
            intentExecutor.executeIntent(
                TestIntent.FailedTransformProducer,
                any()
            )
        } returns flowOf(TestTransform.FailedTransform)

        When("I execute an FailedTransformProducer intent") {
            reducer.executeIntent(TestIntent.FailedTransformProducer)

            Then("The current state should remain the same") {
                reducer.state.value shouldBe TestState.InitialState
            }

            Then("The failure should be logged") {
                verify(exactly = 1) {
                    logger.logFailedTransformNewState(
                        TestTransform.FailedTransform,
                        TestState.InitialState,
                        ofType<Exception>()
                    )
                }
            }
        }
    }

    Given("A class that contains the reducer and it's intent executor has an execution that should run solo") {
        val testFlow = MutableStateFlow(0)

        val reducerWrapper = TestCancellationReducerWrapper(
            someTestFlow = testFlow,
            coroutineListener = coroutineListener
        )

        When("I execute an intent which kills another intent job") {
            reducerWrapper.execute(TestIntent.AbelIntent)
            reducerWrapper.execute(TestIntent.CainIntent)
            testFlow.emit(0)

            Then("State must have been correctly changed") {
                reducerWrapper.state.value.shouldBe(TestState.CainState)
            }
        }

        When("I execute an UniqueIntent twice and trigger an emission on test flow") {
            reducerWrapper.execute(TestIntent.UniqueTransformIntent(IMPOSSIBLE_INTENT_ID))
            reducerWrapper.execute(TestIntent.UniqueTransformIntent(UNIQUE_INTENT_ID))
            testFlow.emit(0)

            Then("Only the last intent must transform the state") {
                reducerWrapper.state.value.shouldBe(TestState.UniqueTransformState)
            }
        }
    }
})
