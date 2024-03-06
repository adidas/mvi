package com.adidas.mvi.transform

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

private data class ReplaceTestNumberContainerObject(val number: Int)

private sealed class ReplaceTestComplexObject {
    data object Child1 : ReplaceTestComplexObject()

    data object Child2 : ReplaceTestComplexObject()

    data object Child3 : ReplaceTestComplexObject()
}

internal class ReplaceTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    given("A list of number containers from 1 to 3") {
        val numberContainers = (1..3).map { ReplaceTestNumberContainerObject(it) }

        `when`("I try to replace containers which have 1 with 0") {
            val replaced =
                numberContainers.replaceIf(equalsOperator(ReplaceTestNumberContainerObject::number, 1)) {
                    it.copy(number = 0)
                }

            then("The first item should have a number 0") {
                replaced.shouldContainInOrder(
                    ReplaceTestNumberContainerObject(0),
                    ReplaceTestNumberContainerObject(2),
                    ReplaceTestNumberContainerObject(3),
                )
            }

            then("Replaced should be a different instance from numberContainers") {
                replaced.shouldNotBe(numberContainers)
            }
        }
    }

    given("A list of children of ReplaceTestComplexObject, only containing Child1 and Child2") {
        val complexObjects = listOf(ReplaceTestComplexObject.Child1, ReplaceTestComplexObject.Child2)

        `when`("I try to replace Child1 with Child3") {
            val replaced =
                complexObjects.replaceIfIs { _: ReplaceTestComplexObject.Child1 ->
                    ReplaceTestComplexObject.Child3
                }

            then("The first item should be Child 3") {
                replaced.shouldContainInOrder(
                    ReplaceTestComplexObject.Child3,
                    ReplaceTestComplexObject.Child2,
                )
            }

            then("Replaced should be a different instance from complexObjects") {
                replaced.shouldNotBe(complexObjects)
            }
        }

        `when`("I try to replace Child1 with Child3 but using a filter which will fail for all items") {
            val replaced =
                complexObjects.replaceIfIs({ false }) { _: ReplaceTestComplexObject.Child1 ->
                    ReplaceTestComplexObject.Child3
                }

            then("No item should be replaced") {
                replaced shouldBe complexObjects
            }
        }
    }
})
