package com.adidas.mvi.declarative.transform.product

import com.adidas.mvi.State

sealed class ProductState : State {
    object Loading : ProductState()
    object Loaded : ProductState()
}
