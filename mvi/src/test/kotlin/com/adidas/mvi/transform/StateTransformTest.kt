package com.adidas.mvi.transform

import com.adidas.mvi.MviState
import com.adidas.mvi.product.FakeProductStateTransform
import com.adidas.mvi.product.ProductSideEffect
import com.adidas.mvi.product.ProductState
import com.adidas.mvi.sideeffects.SideEffects
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StateTransformTest : ShouldSpec({

    context("StateTransform implementation") {
        val state = ProductState.Loaded

        should("use mutate() function for reducing state") {
            val mviState = MviState(ProductState.Loading, SideEffects<ProductSideEffect>())
            FakeProductStateTransform(state).reduce(mviState.state) shouldBe state
        }
    }
})
