package com.adidas.mvi.declarative.transform.product

import com.adidas.mvi.actions.Actions
import com.adidas.mvi.declarative.transform.ActionTransform

class FakeProductActionTransform(private val action: ProductAction) :
    ActionTransform<ProductState, ProductAction>() {

    override fun mutate(actions: Actions<ProductAction>): Actions<ProductAction> {
        return actions.add(action)
    }
}
