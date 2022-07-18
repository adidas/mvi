package com.adidas.mvi.declarative

import com.adidas.mvi.State
import com.adidas.mvi.actions.Actions

public data class MviState<out TState, TAction>(val state: TState, val actions: Actions<TAction>) : State
