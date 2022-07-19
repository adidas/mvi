package com.adidas.mvi

import kotlinx.coroutines.Job
import kotlin.reflect.KClass

@Deprecated(message = "Please use the Reducer to clean the jobs.")
internal class JobTerminatorImpl<TIntent : Intent>(private val multimap: Multimap<TIntent, Job>) : JobTerminator<TIntent> {

    override fun kill(intent: TIntent) {
        multimap[intent].forEach { it.value.cancel(TerminatedIntentException()) }
    }

    override fun killAllFromType(intentClass: KClass<out TIntent>) {
        multimap.keys
            .filter { intentClass.isInstance(it) }
            .forEach(this::kill)
    }
}
