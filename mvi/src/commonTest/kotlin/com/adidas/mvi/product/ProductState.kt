package com.adidas.mvi.product

import com.adidas.mvi.LoggableState

sealed class ProductState : LoggableState {
    data object Loading : ProductState()

    data object Loaded : ProductState()
}
