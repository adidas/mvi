package com.adidas.mvi.transform

import com.adidas.mvi.State
import com.adidas.mvi.product.FakeProductSideEffectTransform
import com.adidas.mvi.product.ProductSideEffect
import com.adidas.mvi.product.ProductState
import com.adidas.mvi.sideeffects.SideEffects
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly

class SideEffectTransformTest : ShouldSpec({

    context("SideEffectTransform") {
        val sideEffect = ProductSideEffect.NavigateToProductDetailsSideEffect

        should("use mutate() function for reducing SideEffects") {
            val state = State(ProductState.Loading, SideEffects<ProductSideEffect>())
            FakeProductSideEffectTransform(sideEffect).reduce(state).sideEffects shouldContainExactly
                listOf<ProductSideEffect>(sideEffect)
        }
    }
})
