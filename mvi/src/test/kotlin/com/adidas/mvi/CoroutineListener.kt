package com.adidas.mvi

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

internal class CoroutineListener(
    internal val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestListener {

    internal val dispatchersContainer: DispatchersContainer = FixedDispatchersContainer(testCoroutineDispatcher)

    override suspend fun beforeContainer(testCase: TestCase) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}
