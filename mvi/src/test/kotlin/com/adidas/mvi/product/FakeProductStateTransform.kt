package com.adidas.mvi.product

import com.adidas.mvi.transform.StateTransform

class FakeProductStateTransform(private val state: ProductState) : StateTransform<ProductState> {

    override fun reduce(currentState: ProductState): ProductState {
        return state
    }
}
