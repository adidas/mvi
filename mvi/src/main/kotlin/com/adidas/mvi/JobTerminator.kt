package com.adidas.mvi

import kotlin.reflect.KClass

@Deprecated(message = "Please use the Reducer to clean the jobs.")
public interface JobTerminator<TIntent : Intent> {

    @Deprecated("Use reducer to clean the jobs")
    public fun kill(intent: TIntent)

    @Deprecated("Use reducer to clean the jobs", replaceWith = ReplaceWith("reducer.cleanIntentJobsOfType()"))
    public fun killAllFromType(intentClass: KClass<out TIntent>)
}
