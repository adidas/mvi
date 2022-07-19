package com.adidas.mvi.declarative.transform.product

sealed class ProductAction {
    object NavigateToProductDetailsAction : ProductAction()
}
