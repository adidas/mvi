package com.adidas.mvi

import com.adidas.mvi.sideeffects.SideEffects

public data class State<out TView, TSideEffect>(val view: TView, val sideEffects: SideEffects<TSideEffect>) : LoggableState
