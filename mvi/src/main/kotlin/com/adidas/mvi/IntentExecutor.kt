package com.adidas.mvi

import com.adidas.mvi.transform.Transform
import kotlinx.coroutines.flow.Flow

public fun interface IntentExecutor<TIntent : Intent, TState : State> {
    public fun executeIntent(intent: TIntent, jobTerminator: JobTerminator<TIntent>): Flow<Transform<TState>>
}
