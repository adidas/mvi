package com.adidas.mvi.transform

import com.adidas.mvi.State
import com.adidas.mvi.product.FakeProductViewTransform
import com.adidas.mvi.product.ProductSideEffect
import com.adidas.mvi.product.ProductState
import com.adidas.mvi.sideeffects.SideEffects
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StateTransformTest : ShouldSpec({

    context("StateTransform implementation") {
        val state = ProductState.Loaded

        should("use mutate() function for reducing state") {
            val state = State(ProductState.Loading, SideEffects<ProductSideEffect>())
            FakeProductViewTransform(state).reduce(state).view shouldBe state.view
        }
    }
})
