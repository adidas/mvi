package com.adidas.mvi.reducer

import com.adidas.mvi.CoroutineListener
import com.adidas.mvi.Reducer
import com.adidas.mvi.reducer.logger.SpyLogger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestScope

internal class TestCancellationReducerWrapper(
        private val someTestFlow: Flow<Int>,
        coroutineListener: CoroutineListener
) {

    private val reducer = Reducer(
            coroutineScope = TestScope(coroutineListener.testCoroutineDispatcher),
            initialState = TestState.InitialState,
            defaultDispatcher = coroutineListener.testCoroutineDispatcher,
            logger = SpyLogger(),
            intentExecutor = this::executeIntent
    )
    val state = reducer.state

    fun execute(intent: TestIntent) {
        reducer.executeIntent(intent)
    }

    private fun executeIntent(
            intent: TestIntent
    ): Flow<TestTransform> {
        return when (intent) {
            TestIntent.AbelIntent -> someTestFlow.map { TestTransform.AbelTransform }
            TestIntent.CainIntent -> flowOf(TestTransform.CainTransform).onStart { reducer.cleanIntentJobsOfType(TestIntent.AbelIntent::class) }
            is TestIntent.UniqueTransformIntent -> someTestFlow.map { TestTransform.UniqueTransform(intent.id) }
            else -> emptyFlow()
        }
    }
}
