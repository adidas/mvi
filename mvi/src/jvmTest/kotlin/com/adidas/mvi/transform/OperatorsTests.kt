package com.adidas.mvi.transform

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

private data class OperatorsTestObject(val number: Int)

internal class OperatorsTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    given("A function that returns a boolean") {
        val function: (Unit) -> Boolean = {
            true
        }

        `when`("I use it with not") {
            val result = notOperator(function)

            then("It should be negated") {
                result(Unit).shouldBeFalse()
            }
        }
    }

    given("An instance of OperatorsTestObject") {
        val operatorsTestObject = OperatorsTestObject(number = 1)

        `when`("I test that it has the number 1 using equals") {
            val result = equalsOperator(OperatorsTestObject::number, 1)(operatorsTestObject)

            then("It should return true") {
                result.shouldBeTrue()
            }
        }

        `when`("I test that it doesn't have the number 1 using notEquals") {
            val result = notEqualsOperator(OperatorsTestObject::number, 1)(operatorsTestObject)

            then("It should return false") {
                result.shouldBeFalse()
            }
        }
    }
})
