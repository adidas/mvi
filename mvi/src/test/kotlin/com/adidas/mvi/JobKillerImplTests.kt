package com.adidas.mvi

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.coroutines.Job

private sealed class ParentIntent : Intent {
    object FakeIntent1 : ParentIntent()

    object FakeIntent2 : ParentIntent()
}

internal class JobKillerImplTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    given("A JobKiller instance with some jobs") {
        val multimap = Multimap<ParentIntent, Job>()
        multimap.put(ParentIntent.FakeIntent1, Job())
        multimap.put(ParentIntent.FakeIntent1, Job())
        multimap.put(ParentIntent.FakeIntent2, Job())
        multimap.put(ParentIntent.FakeIntent2, Job())
        val intentKiller = JobTerminatorImpl(multimap)

        `when`("I kill a specific intent") {
            intentKiller.kill(ParentIntent.FakeIntent1)

            then("Its job should be killed") {
                multimap[ParentIntent.FakeIntent1].forAll {
                    it.value.isCancelled.shouldBeTrue()
                }
            }

            then("Jobs for other intents should not be canceled") {
                multimap[ParentIntent.FakeIntent2].forAll {
                    it.value.isCancelled.shouldBeFalse()
                }
            }
        }

        `when`("I kill all intents of a specific supertype") {
            intentKiller.killAllFromType(ParentIntent::class)

            then("All remaining jobs related to a intent, whose supertype is the canceled one should be killed") {
                multimap.keys.forAll { intent ->
                    multimap[intent].forAll { entry ->
                        entry.value.isCancelled.shouldBeTrue()
                    }
                }
            }
        }
    }
})
