package com.adidas.mvi

public interface Logger {
    public fun logIntent(intent: Loggable)
    public fun logFailedIntent(intent: Loggable, throwable: Throwable)
    public fun logTransformedNewState(transform: Loggable, previousState: Loggable, newState: Loggable)
    public fun logFailedTransformNewState(transform: Loggable, state: Loggable, throwable: Throwable)
}
