package com.adidas.mvi.declarative.transform

import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.declarative.transform.product.FakeProductStateTransform
import com.adidas.mvi.declarative.transform.product.ProductSideEffect
import com.adidas.mvi.declarative.transform.product.ProductState
import com.adidas.mvi.sideeffects.SideEffects
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StateTransformTest : ShouldSpec({

    context("StateTransform implementation") {
        val state = ProductState.Loaded

        should("use mutate() function for reducing state") {
            val mviState = MviState(ProductState.Loading, SideEffects<ProductSideEffect>())
            FakeProductStateTransform(state).reduce(mviState).state shouldBe state
        }
    }
})
