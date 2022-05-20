package com.adidas.mvi

import kotlinx.coroutines.CancellationException

internal class TerminatedIntentException : CancellationException()
