package com.adidas.mvi.declarative.transform.product

import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.sideeffects.SideEffects

class FakeProductSideEffectTransform(private val sideEffect: ProductSideEffect) :
    MviState.SideEffectTransform<ProductState, ProductSideEffect>() {

    override fun mutate(sideEffects: SideEffects<ProductSideEffect>): SideEffects<ProductSideEffect> {
        return sideEffects.add(sideEffect)
    }
}
