package com.adidas.mvi.sideeffects

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainInOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
internal class SideEffectsTest : BehaviorSpec({
    given("An empty SideEffects") {
        val sideEffects = SideEffects<TestSideEffect>()

        `when`("I try to read it") {
            then("It should be empty") {
                sideEffects.shouldBeEmpty()
            }
        }

        `when`("I add one SideEffect") {
            val addedSideEffect = TestSideEffect()
            val returnedSideEffectContainer = sideEffects.add(addedSideEffect)

            then("The SideEffect should be returned") {
                returnedSideEffectContainer shouldContain addedSideEffect
            }

            then("The SideEffect should not be returned twice") {
                returnedSideEffectContainer.shouldBeEmpty()
            }
        }

        `when`("I add two SideEffects") {
            val firstSideEffect = TestSideEffect()
            val secondSideEffect = TestSideEffect()
            val addedSideEffects = sideEffects.add(firstSideEffect, secondSideEffect)

            then("Both SideEffects should be returned in order of addition") {
                addedSideEffects shouldContainInOrder listOf(firstSideEffect, secondSideEffect)
            }

            then("No SideEffect should be returned anymore") {
                addedSideEffects.shouldBeEmpty()
            }
        }

        `when`("I add one SideEffect and clear it") {
            val firstSideEffect = TestSideEffect()

            sideEffects.add(firstSideEffect)
            val clearedSideEffects = sideEffects.clear()

            then("No SideEffects should be returned") {
                clearedSideEffects.shouldBeEmpty()
            }
        }

        `when`("I try to read SideEffects and it takes time, simulated by a semaphore") {
            val firstSideEffect = TestSideEffect()
            val secondSideEffectToBeAddedLater = TestSideEffect()

            var returnedSideEffects = sideEffects.add(firstSideEffect)

            val semaphore = Semaphore(2)

            val readJob = launch(Dispatchers.Default) {
                returnedSideEffects.forEach { _ ->
                    semaphore.acquire() // Wait for the signal
                }
            }

            val addJob = launch(Dispatchers.Default) {
                returnedSideEffects = sideEffects.add(secondSideEffectToBeAddedLater)
            }

            semaphore.release()

            then("It should be released only by the semaphore").config(
                timeout =
                5.toDuration(
                    DurationUnit.SECONDS,
                ),
            ) {
                readJob.join()
                addJob.join()

                readJob.isActive.shouldBeFalse()
                addJob.isActive.shouldBeFalse()
                returnedSideEffects.shouldContain(secondSideEffectToBeAddedLater)
            }
        }
    }
})
