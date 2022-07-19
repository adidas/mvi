package com.adidas.mvi

import kotlinx.coroutines.CoroutineDispatcher

internal class FixedDispatchersContainer(
    coroutineDispatcher: CoroutineDispatcher
) : DispatchersContainer {
    override val Default: CoroutineDispatcher = coroutineDispatcher
    override val Main: CoroutineDispatcher = coroutineDispatcher
    override val Unconfined: CoroutineDispatcher = coroutineDispatcher
    override val IO: CoroutineDispatcher = coroutineDispatcher
}
