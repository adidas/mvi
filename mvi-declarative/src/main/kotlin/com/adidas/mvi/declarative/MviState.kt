package com.adidas.mvi.declarative

import com.adidas.mvi.LoggableState
import com.adidas.mvi.sideeffects.SideEffects

public data class MviState<out TState, TSideEffect>(val state: TState, val sideEffects: SideEffects<TSideEffect>) : LoggableState
