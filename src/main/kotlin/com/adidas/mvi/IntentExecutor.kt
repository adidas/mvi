package com.adidas.mvi

import kotlinx.coroutines.flow.Flow

public fun interface IntentExecutor<TIntent : Intent, TTransform> {
    public fun executeIntent(intent: TIntent, jobTerminator: JobTerminator<TIntent>): Flow<TTransform>
}
