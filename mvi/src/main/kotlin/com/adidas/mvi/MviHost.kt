package com.adidas.mvi

import kotlinx.coroutines.flow.Flow

/**
 * Apply this interface to anything you want to become an Mvi host,
 * it can be an Android ViewModel or types of presenters etc.
 *
 */
public interface MviHost<in TIntent : Intent, out TState : LoggableState> {

    public val state: Flow<TState>

    public fun execute(intent: TIntent)
}
