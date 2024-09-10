package com.adidas.mvi

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

internal class MultimapTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    given("An empty multimap") {
        val multimap = Multimap<String, Int>()

        `when`("I put a value") {
            val entry = multimap.put("Test", 1)

            then("The value should be returned") {
                entry shouldBe MultimapEntry("Test", 1)
            }

            then("The value should be in the map") {
                assertSoftly(multimap["Test"]) {
                    shouldHaveSize(1)
                    shouldContain(MultimapEntry("Test", 1))
                }
            }

            then("A key should be returned") {
                assertSoftly(multimap.keys) {
                    shouldHaveSize(1)
                    shouldContain("Test")
                }
            }
        }

        `when`("I try to get a key which has no values") {
            val entries = multimap["NotAdded"]

            then("The entries should be empty") {
                entries.shouldBeEmpty()
            }
        }
    }

    given("A multimap with an already added value") {
        val multimap = Multimap<String, Int>()
        multimap.put("Test", 1)

        `when`("I put another value on the same key") {
            multimap.put("Test", 2)

            then("The other value should be added also") {
                assertSoftly(multimap["Test"]) {
                    shouldHaveSize(2)
                    shouldContain(MultimapEntry("Test", 2))
                }
            }
        }
    }

    given("A multimap with 2 values added on the same key") {
        val multimap = Multimap<String, Int>()
        multimap.put("Test", 1)
        multimap.put("Test", 2)

        `when`("I remove an entry") {
            multimap.remove(MultimapEntry("Test", 1))

            then("The value should not be returned anymore") {
                assertSoftly(multimap["Test"]) {
                    shouldHaveSize(1)
                    shouldNotContain(MultimapEntry("Test", 1))
                }
            }
        }

        `when`("I remove all entries") {
            multimap.remove(MultimapEntry("Test", 1))
            multimap.remove(MultimapEntry("Test", 2))

            then("The key should be removed") {
                multimap.keys shouldNotContain "Test"
            }
        }
    }

    given("A filled multimap") {
        val original = Multimap<String, Int>()
        original.put("Test", 1)

        `when`("I copy it and add a value on the original") {
            val copied = original.copy()
            original.put("Test", 2)

            then("The copied should not contain the added value") {
                assertSoftly(copied["Test"]) {
                    shouldHaveSize(1)
                    shouldNotContain(MultimapEntry("Test", 2))
                }
            }
        }

        `when`("I copy it and remove a value on the original") {
            val copied = original.copy()
            original.remove(MultimapEntry("Test", 1))

            then("The copied should still contain the removed entry") {
                assertSoftly(copied["Test"]) {
                    shouldHaveSize(1)
                    shouldContain(MultimapEntry("Test", 1))
                }
            }
        }
    }
})