package com.adidas.mvi.declarative.transform

import com.adidas.mvi.actions.Actions
import com.adidas.mvi.declarative.MviState
import com.adidas.mvi.declarative.transform.product.FakeProductActionTransform
import com.adidas.mvi.declarative.transform.product.ProductAction
import com.adidas.mvi.declarative.transform.product.ProductState
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly

class ActionTransformTest : ShouldSpec({

    context("ActionTransform") {
        val action = ProductAction.NavigateToProductDetailsAction

        should("use mutate() function for reducing actions") {
            val mviState = MviState(ProductState.Loading, Actions<ProductAction>())
            FakeProductActionTransform(action).reduce(mviState).actions shouldContainExactly
                listOf<ProductAction>(action)
        }
    }
})
