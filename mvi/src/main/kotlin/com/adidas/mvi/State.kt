package com.adidas.mvi

import com.adidas.mvi.sideeffects.SideEffects

public data class State<out TState, TSideEffect>(val view: TState, val sideEffects: SideEffects<TSideEffect>) : LoggableState
