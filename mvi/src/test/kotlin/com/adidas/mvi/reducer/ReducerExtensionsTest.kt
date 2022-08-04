package com.adidas.mvi.reducer

import com.adidas.mvi.Intent
import com.adidas.mvi.State
import com.adidas.mvi.product.ProductState
import com.adidas.mvi.transform.StateTransform
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.TestScope

class ReducerExtensionsTest : ShouldSpec({
    context("A reducer instantiated with the extension") {
        val reducer = Reducer(
            coroutineScope = TestScope(),
            initialInnerState = ProductState.Loading,
            intentExecutor = { _: Intent ->
                emptyFlow<StateTransform<State<ProductState, Unit>>>()
            }
        )

        should("The initial inner state should be Loading") {
            reducer.state.value.view shouldBe ProductState.Loading
        }
    }
})
