package com.adidas.mvi.product

import com.adidas.mvi.State
import com.adidas.mvi.transform.StateTransform

class FakeProductStateTransform(private val state: State<ProductState, ProductSideEffect>) :
    StateTransform<ProductState> {

    override fun reduce(currentState: ProductState): ProductState {
        return state.view
    }
}
