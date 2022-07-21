package com.adidas.mvi.declarative.transform.product

sealed class ProductSideEffect {
    object NavigateToProductDetailsSideEffect : ProductSideEffect()
}
