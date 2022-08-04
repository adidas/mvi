package com.adidas.mvi.product

import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.SideEffectTransform

class FakeProductSideEffectTransform(private val sideEffect: ProductSideEffect) :
    SideEffectTransform<ProductState, ProductSideEffect>() {

    override fun mutate(sideEffects: SideEffects<ProductSideEffect>): SideEffects<ProductSideEffect> {
        return sideEffects.add(sideEffect)
    }
}
