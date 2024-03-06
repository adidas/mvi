package com.adidas.mvi.product

import com.adidas.mvi.State
import com.adidas.mvi.transform.ViewTransform

class FakeProductViewTransform(private val state: State<ProductState, ProductSideEffect>) :
    ViewTransform<ProductState, ProductSideEffect>() {
    override fun mutate(currentState: ProductState): ProductState {
        return state.view
    }
}
