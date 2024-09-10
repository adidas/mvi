package com.adidas.mvi

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

internal class CoroutineListener(
    internal val testCoroutineDispatcher: TestDispatcher = UnconfinedTestDispatcher(
        TestCoroutineScheduler()
    ),
) : TestListener {

    internal val dispatchersContainer: DispatchersContainer =
        FixedDispatchersContainer(testCoroutineDispatcher)

    override suspend fun beforeContainer(testCase: TestCase) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
        testCoroutineDispatcher.scheduler.cancel()
    }

    public fun advanceUntilIdle() {
        testCoroutineDispatcher.scheduler.advanceUntilIdle()
    }
}
