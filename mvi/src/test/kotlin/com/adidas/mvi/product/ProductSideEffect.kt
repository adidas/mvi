package com.adidas.mvi.product

sealed class ProductSideEffect {
    object NavigateToProductDetailsSideEffect : ProductSideEffect()
}
