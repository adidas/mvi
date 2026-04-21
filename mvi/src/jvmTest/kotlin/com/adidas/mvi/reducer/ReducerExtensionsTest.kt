package com.adidas.mvi.reducer

import com.adidas.mvi.Intent
import com.adidas.mvi.State
import com.adidas.mvi.product.FakeProductViewTransform
import com.adidas.mvi.product.ProductSideEffect
import com.adidas.mvi.product.ProductState
import com.adidas.mvi.sideeffects.SideEffects
import com.adidas.mvi.transform.StateTransform
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.engine.coroutines.backgroundScope
import io.kotest.engine.coroutines.testScheduler
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.yield

class ReducerExtensionsTest : ShouldSpec(
    {
        coroutineTestScope = true

        context("A reducer instantiated with the extension") {
            val reducer =
                Reducer(
                    coroutineScope = TestScope(),
                    initialInnerState = ProductState.Loading,
                    intentExecutor = { _: Intent ->
                        emptyFlow<StateTransform<State<ProductState, Unit>>>()
                    },
                )

            should("The initial inner state should be Loading") {
                reducer.state.value.view shouldBe ProductState.Loading
            }
        }

        context("A reducer instantiated ") {
            val reducer =
                Reducer<Intent, ProductState, ProductSideEffect>(
                    coroutineScope = backgroundScope,
                    initialInnerState = ProductState.Loading,
                    intentExecutor = { intent: Intent ->
                        println("got intent $intent")
                        if (intent is TestIntent.CainIntent) {
                            flowOf<StateTransform<State<ProductState, ProductSideEffect>>>(FakeProductViewTransform(State(ProductState.Loaded, SideEffects())))
                        } else {
                            emptyFlow<StateTransform<State<ProductState, ProductSideEffect>>>()
                        }
                    },
                )

            val awaitJob = backgroundScope.async { reducer.awaitView<ProductState.Loaded, _>() }
            testScheduler.runCurrent()

            should("The initial await state should not return yet") {
                awaitJob.isCompleted shouldBe false
            }


            should("return after emitting Loaded") {
                reducer.executeIntent(TestIntent.CainIntent)
                testScheduler.runCurrent()

                awaitJob.isCompleted shouldBe true
                awaitJob.await() shouldBe ProductState.Loaded
            }
        }
    },
)
