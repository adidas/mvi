package com.adidas.mvi.sample.login.viewmodel

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
internal class CoroutineListener(
    private val testCoroutineDispatcher: TestDispatcher,
) : TestListener {

    override suspend fun beforeContainer(testCase: TestCase) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
        testCoroutineDispatcher.scheduler.cancel()
    }
}
