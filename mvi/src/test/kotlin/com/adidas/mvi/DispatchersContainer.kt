package com.adidas.mvi

import kotlinx.coroutines.CoroutineDispatcher

internal interface DispatchersContainer {
    val Default: CoroutineDispatcher
    val Main: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
    val IO: CoroutineDispatcher
}
