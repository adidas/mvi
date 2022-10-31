package com.adidas.mvi

import com.adidas.mvi.transform.StateTransform
import kotlinx.coroutines.flow.Flow

public fun interface IntentExecutor<TIntent : Intent, TState : LoggableState> {
    public fun executeIntent(intent: TIntent): Flow<StateTransform<TState>>
}
