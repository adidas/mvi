package com.adidas.mvi.reducer

import com.adidas.mvi.CoroutineListener
import com.adidas.mvi.Reducer
import com.adidas.mvi.State
import com.adidas.mvi.TerminatedIntentException
import com.adidas.mvi.reducer.logger.SpyLogger
import com.adidas.mvi.reducer.logger.shouldContainFailingIntent
import com.adidas.mvi.reducer.logger.shouldContainFailingTransform
import com.adidas.mvi.reducer.logger.shouldContainSuccessfulIntent
import com.adidas.mvi.reducer.logger.shouldContainSuccessfulTransform
import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.StateTransform
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.shouldForAtLeastOne
import io.kotest.inspectors.shouldForNone
import io.kotest.inspectors.shouldForOne
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope

internal class ReducerTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val logger = SpyLogger()
    val coroutineListener = CoroutineListener()
    listeners(coroutineListener)

    fun createReducer(
        executor: (intent: TestIntent) -> Flow<StateTransform<State<TestState, TestSideEffect>>> =
            createIntentExecutorContainer(
                transform = TestTransform.Transform1,
            ),
    ) = Reducer(
        coroutineScope = TestScope(coroutineListener.testCoroutineDispatcher),
        initialState = State(TestState.InitialState, SideEffects()),
        defaultDispatcher = coroutineListener.testCoroutineDispatcher,
        logger = logger,
        intentExecutor = executor,
    )

    Given("A reducer with a initial state") {
        val executedIntents = mutableListOf<TestIntent>()
        val executor: (intent: TestIntent) -> Flow<StateTransform<State<TestState, TestSideEffect>>> =
            createIntentExecutorContainer(executedIntents)
        val reducer = createReducer(executor)

        When("I listen to its first value") {
            val firstValue = reducer.state.value

            Then("It should be the initial state") {
                firstValue.view shouldBe TestState.InitialState
            }
        }

        When("I try to require a state different than the current state") {
            Then("It should cause a ClassCastException") {
                shouldThrow<ClassCastException> {
                    reducer.requireView<TestState.StateFromTransform1>()
                }
            }
        }

        When("I try to require a state equal to the current state") {
            val requiredView = reducer.requireView<TestState.InitialState>()

            Then("It should return the cast state") {
                requiredView shouldBe TestState.InitialState
            }
        }

        When("I execute an intent") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            Then("This intent should be logged") {
                logger.history.shouldForOne { log ->
                    log shouldContainSuccessfulIntent TestIntent.SimpleIntent.toString()
                }
                executedIntents shouldContain TestIntent.SimpleIntent
            }
        }
    }

    Given("A reducer which triggers a TerminatedIntentException when executing a SimpleIntent") {
        val exceptionToThrow = TerminatedIntentException()
        val reducer = createReducer(createIntentExecutorContainer(exception = exceptionToThrow))

        When("I execute an intent and it is cancelled throwing TerminatedIntentException") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            Then("The exception should NOT be logged") {
                logger.history.shouldForNone { log ->
                    log shouldContainSuccessfulIntent exceptionToThrow.toString()
                }
            }
        }
    }

    Given("A reducer which fails to execute a SimpleIntent") {
        val exceptionToThrow = Exception()
        val reducer = createReducer(createIntentExecutorContainer(exception = exceptionToThrow))

        When("I execute an intent") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            Then("The exception should be logged") {
                logger.history.shouldForAtLeastOne { log ->
                    log shouldContainFailingIntent exceptionToThrow.toString()
                }
            }
        }
    }

    Given("A reducer which produces Transform1 partial state when Transform1Producer intent is sent") {
        val reducer =
            createReducer(
                createIntentExecutorContainer(
                    intent = TestIntent.Transform1Producer,
                    transform = TestTransform.Transform1,
                ),
            )

        When("I execute Transform1Producer intent") {
            reducer.executeIntent(TestIntent.Transform1Producer)

            Then("The state should change to the state which Transform1 produces") {
                reducer.state.value.view shouldBe TestState.StateFromTransform1
                logger.history.shouldForOne { log ->
                    log shouldContainSuccessfulTransform TestState.StateFromTransform1.toString()
                }
            }
        }
    }

    Given("A reducer which produces FailedTransform partial state when FailedTransformProducer intent is sent") {
        val transform = TestTransform.FailedTransform
        val reducer = createReducer(createIntentExecutorContainer(transform = transform))

        When("I execute an FailedTransformProducer intent") {
            reducer.executeIntent(TestIntent.FailedTransformProducer)

            Then("The current state should remain the same") {
                reducer.state.value.view shouldBe TestState.InitialState
            }

            Then("The failure should be logged") {
                logger.history.shouldForOne { log ->
                    log shouldContainFailingTransform transform.toString()
                }
            }
        }
    }

    Given("A class that contains the reducer and it's intent executor has an execution that should run solo") {
        val testFlow = MutableStateFlow(0)

        val reducerWrapper =
            TestCancellationReducerWrapper(
                someTestFlow = testFlow,
                coroutineListener = coroutineListener,
            )

        When("I execute an intent which kills another intent job") {
            reducerWrapper.execute(TestIntent.AbelIntent)
            reducerWrapper.execute(TestIntent.CainIntent)
            testFlow.emit(0)

            Then("State must have been correctly changed") {
                reducerWrapper.state.value.view.shouldBe(TestState.CainState)
            }
        }

        When("I execute an UniqueIntent twice and trigger an emission on test flow") {
            reducerWrapper.execute(TestIntent.UniqueTransformIntent(IMPOSSIBLE_INTENT_ID))
            reducerWrapper.execute(TestIntent.UniqueTransformIntent(UNIQUE_INTENT_ID))
            testFlow.emit(0)

            Then("Only the last intent must transform the state") {
                reducerWrapper.state.value.view.shouldBe(TestState.UniqueTransformState)
            }
        }
    }
})

private fun createIntentExecutorContainer(executedIntents: MutableList<TestIntent> = mutableListOf()): (TestIntent) -> Flow<StateTransform<State<TestState, TestSideEffect>>> =
    {
        executedIntents.add(it)
        flowOf(TestTransform.Transform1)
    }

private fun createIntentExecutorContainer(exception: Throwable): (TestIntent) -> Flow<StateTransform<State<TestState, TestSideEffect>>> =
    {
        if (it is TestIntent.SimpleIntent) throw exception
        emptyFlow()
    }

private fun createIntentExecutorContainer(
    intent: TestIntent = TestIntent.FailedTransformProducer,
    transform: TestTransform,
): (TestIntent) -> Flow<StateTransform<State<TestState, TestSideEffect>>> =
    {
        when (it) {
            intent -> {
                flowOf(transform)
            }

            else -> emptyFlow()
        }
    }
