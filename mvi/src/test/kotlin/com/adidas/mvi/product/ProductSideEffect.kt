package com.adidas.mvi.product

sealed class ProductSideEffect {
    data object NavigateToProductDetailsSideEffect : ProductSideEffect()
}
