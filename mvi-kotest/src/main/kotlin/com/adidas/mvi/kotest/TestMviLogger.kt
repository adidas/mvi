package com.adidas.mvi.kotest

import com.adidas.mvi.Loggable
import com.adidas.mvi.Logger

/**
 * A fake implementation of [Logger] that remembers all logged entries, for use in tests.
 *
 * You can access list of all logged entries from [loggedTranformations] and [loggedIntents].
 */
public class TestMviLogger : Logger {
    public val loggedTranformations: MutableList<LoggedTranformation> = ArrayList<LoggedTranformation>()
    public val loggedIntents: MutableList<LoggedIntent> = ArrayList<LoggedIntent>()

    override fun logFailedIntent(
        intent: Loggable,
        throwable: Throwable,
    ) {
        loggedIntents += LoggedIntent.Failure(intent, throwable)
    }

    override fun logIntent(intent: Loggable) {
        loggedIntents += LoggedIntent.Success(intent)
    }

    override fun logFailedTransformNewState(
        transform: Loggable,
        state: Loggable,
        throwable: Throwable,
    ) {
        loggedTranformations += LoggedTranformation.Failure(transform, state, throwable)
    }

    override fun logTransformedNewState(
        transform: Loggable,
        previousState: Loggable,
        newState: Loggable,
    ) {
        loggedTranformations += LoggedTranformation.Success(transform, previousState, newState)
    }

    public sealed class LoggedIntent() {
        public abstract val intent: Loggable

        public data class Success(override val intent: Loggable) : LoggedIntent()

        public data class Failure(override val intent: Loggable, val throwable: Throwable) : LoggedIntent()
    }

    public sealed class LoggedTranformation() {
        public abstract val transform: Loggable
        public abstract val previousState: Loggable

        public data class Success(override val transform: Loggable, override val previousState: Loggable, val newState: Loggable) : LoggedTranformation()

        public data class Failure(override val transform: Loggable, override val previousState: Loggable, val throwable: Throwable) : LoggedTranformation()
    }
}
