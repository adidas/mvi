package com.adidas.mvi

import com.adidas.mvi.transform.StateTransform
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

public typealias SimplifiedIntentExecutor<TIntent, TState> = (intent: TIntent) -> Flow<StateTransform<TState>>

public class Reducer<TIntent, TState> @Deprecated("Use the other constructor, this option will be deprecated in the future") constructor(
    private val coroutineScope: CoroutineScope,
    initialState: TState,
    private val logger: Logger? = null,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val intentExecutor: IntentExecutor<TIntent, TState>
) where TIntent : Intent,
        TState : State {
    @Suppress("DEPRECATION")
    public constructor(
        coroutineScope: CoroutineScope,
        initialState: TState,
        intentExecutor: SimplifiedIntentExecutor<TIntent, TState>,
        logger: Logger? = null,
        defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : this(coroutineScope, initialState, logger, defaultDispatcher, { intent, _ ->
        intentExecutor(intent)
    })

    private val _transforms = MutableSharedFlow<StateTransform<TState>>()
    // TODO 2: use another (simpler) data structure once we remove jobTerminator dependency (1) - https://tools.adidas-group.com/jira/browse/HXC-819 [2]
    private val multimap = Multimap<TIntent, Job>()

    public val state: StateFlow<TState> = _transforms
        .scan(initialState, this::reduce)
        .stateIn(coroutineScope, SharingStarted.Eagerly, initialState)

    public fun executeIntent(intent: TIntent) {
        logger?.logIntent(intent)

        // TODO 1 : remove this once we replace in all view models - https://tools.adidas-group.com/jira/browse/HXC-819 [1]
        val snapshotOfJobs = multimap.copy()
        val intentKiller = JobTerminatorImpl(snapshotOfJobs)

        val job = coroutineScope.launch {
            if (intent is UniqueIntent) {
                cleanIntentJobsOfType(intent::class)
            }

            try {
                _transforms.emitAll(intentExecutor.executeIntent(intent, intentKiller))
            } catch (throwable: Throwable) {
                if (coroutineScope.isActive && throwable !is TerminatedIntentException) {
                    logger?.logFailedIntent(intent, throwable)
                }
            }
        }

        val entry = multimap.put(intent, job)

        job.invokeOnCompletion { multimap.remove(entry) }
    }

    public suspend fun <T : TIntent> cleanIntentJobsOfType(intentClass: KClass<T>) {
        val uniqueJob = coroutineContext[Job]
        multimap[intentClass].forEach {
            if (it.value != uniqueJob) {
                it.value.cancel(TerminatedIntentException())
            }
        }
    }

    public inline fun <reified T : TState> requireState(): T = state.value as T

    @Suppress("FunctionName")
    private suspend fun reduce(previousState: TState, transform: StateTransform<TState>): TState {
        return try {
            transform.reduce(previousState, defaultDispatcher).also { newState ->
                logger?.logTransformedNewState(transform, previousState, newState)
            }
        } catch (throwable: Throwable) {
            logger?.logFailedTransformNewState(transform, previousState, throwable)
            previousState
        }
    }
}
