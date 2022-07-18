package com.adidas.mvi.declarative.transform.product

import com.adidas.mvi.declarative.transform.StateTransform

class FakeProductStateTransform(private val state: ProductState) : StateTransform<ProductState, ProductAction>() {

    override fun mutate(currentState: ProductState): ProductState {
        return state
    }
}
