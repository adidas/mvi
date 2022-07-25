package com.adidas.mvi.declarative.transform.product

import com.adidas.mvi.declarative.MviState

class FakeProductStateTransform(private val state: ProductState) : MviState.StateTransform<ProductState, ProductSideEffect>() {

    override fun mutate(currentState: ProductState): ProductState {
        return state
    }
}
