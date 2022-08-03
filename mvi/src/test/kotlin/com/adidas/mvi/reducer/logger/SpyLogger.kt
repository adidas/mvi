package com.adidas.mvi.reducer.logger

import com.adidas.mvi.Loggable
import com.adidas.mvi.Logger
import java.lang.StringBuilder

private const val SPACE = " "
internal const val SUCCESSFUL_INTENT = "SuccessfulIntent:"
internal const val FAILED_INTENT = "FailedIntent:"
internal const val SUCCESSFUL_TRANSFORM = "SuccessfulTransform:"
internal const val FAILED_TRANSFORM = "FailedTransform:"

class SpyLogger : Logger {

    var history = mutableListOf<String>()

    override fun logIntent(intent: Loggable) {
        log(intent.toString())
        log(
            StringBuilder().apply {
                append(SUCCESSFUL_INTENT)
                append(intent.toString())
            }.toString()
        )
    }

    override fun logFailedIntent(intent: Loggable, throwable: Throwable) {
        log(
            StringBuilder().apply {
                append(FAILED_INTENT)
                append(intent.toString())
                append(SPACE)
                append(throwable)
            }.toString()
        )
    }

    override fun logTransformedNewState(transform: Loggable, previousState: Loggable, newState: Loggable) {
        log(
            StringBuilder().apply {
                append(SUCCESSFUL_TRANSFORM)
                append(transform.toString())
                append(SPACE)
                append(previousState.toString())
                append(SPACE)
                append(newState.toString())
            }.toString()
        )
    }

    override fun logFailedTransformNewState(transform: Loggable, state: Loggable, throwable: Throwable) {
        log(
            StringBuilder().apply {
                append(FAILED_TRANSFORM)
                append(transform.toString())
                append(SPACE)
                append(state.toString())
                append(SPACE)
                append(throwable)
            }.toString()
        )
    }

    private fun log(message: String) {
        history.add(message)
    }
}
