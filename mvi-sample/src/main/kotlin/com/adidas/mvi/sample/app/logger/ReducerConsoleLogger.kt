package com.adidas.mvi.sample.app.logger

import com.adidas.mvi.Loggable
import com.adidas.mvi.Logger

internal class ReducerConsoleLogger : Logger {

    override fun logIntent(intent: Loggable) {
        println("Intent: ${getLoggableName(intent)}")
    }

    override fun logFailedIntent(
        intent: Loggable,
        throwable: Throwable,
    ) {
        println("[Error]Intent: ${getLoggableName(intent)} Exception: ${throwable::class.qualifiedName}")
        throwable.printStackTrace()
    }

    override fun logTransformedNewState(
        transform: Loggable,
        previousState: Loggable,
        newState: Loggable,
    ) {
        println(
            "Reduce: Using: ${getLoggableName(transform)}, On: ${
                getLoggableName(
                    previousState
                )
            }, To: ${getLoggableName(newState)}"
        )
    }

    override fun logFailedTransformNewState(
        transform: Loggable,
        state: Loggable,
        throwable: Throwable,
    ) {
        println(
            "[Error]Reduce: Using: ${getLoggableName(transform)} On:${
                getLoggableName(
                    state
                )
            } Exception: ${throwable::class.qualifiedName}"
        )
        throwable.printStackTrace()
    }

    private fun getLoggableName(loggable: Loggable): String? {
        val qualifiedName = loggable::class.qualifiedName
        val packageName = loggable.javaClass.`package`?.name

        return qualifiedName?.substringAfter("$packageName.") ?: "N/D"
    }
}