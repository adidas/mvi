package com.adidas.mvi.actions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainInOrder
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

private class TestActionForActions

@ExperimentalTime
internal class ActionsTests : BehaviorSpec({
    given("A empty actions") {
        val actions = Actions<TestActionForActions>()

        `when`("I try to read it") {
            then("It should be empty") {
                actions.shouldBeEmpty()
            }
        }

        `when`("I add one action") {
            val addedAction = TestActionForActions()
            val returnedActionContainer = actions.add(addedAction)

            then("The action should be returned") {
                returnedActionContainer shouldContain addedAction
            }

            then("The action should not be returned twice") {
                returnedActionContainer.shouldBeEmpty()
            }
        }

        `when`("I add two actions") {
            val firstAction = TestActionForActions()
            val secondAction = TestActionForActions()
            val newActions = actions.add(firstAction, secondAction)

            then("Both actions should be returned in order of addition") {
                newActions shouldContainInOrder listOf(firstAction, secondAction)
            }

            then("No action should be returned anymore") {
                newActions.shouldBeEmpty()
            }
        }

        `when`("I add one action and clear it") {
            val firstAction = TestActionForActions()

            actions.add(firstAction)
            val returnedActionContainer = actions.clear()

            then("No action should be returned") {
                returnedActionContainer.shouldBeEmpty()
            }
        }

        `when`("I try to read actions and it takes time, simulated by a semaphore") {
            val firstAction = TestActionForActions()
            val secondActionToBeAddedLater = TestActionForActions()

            var returnedActions = actions.add(firstAction)

            val semaphore = Semaphore(0)

            val readThread = thread {
                returnedActions.forEach { _ ->
                    semaphore.acquire()
                }
            }

            val addThread = thread {
                returnedActions = returnedActions.add(secondActionToBeAddedLater)
            }

            semaphore.release()

            then("It should be released only by the semaphore").config(timeout = 5.toDuration(DurationUnit.SECONDS)) {
                @Suppress("BlockingMethodInNonBlockingContext")
                readThread.join()
                @Suppress("BlockingMethodInNonBlockingContext")
                addThread.join()

                readThread.isAlive.shouldBeFalse()
                addThread.isAlive.shouldBeFalse()
                returnedActions.shouldContain(secondActionToBeAddedLater)
            }
        }
    }
})
