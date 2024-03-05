package com.adidas.mvi.sideeffects

import java.util.LinkedList
import java.util.Queue

/**
 * A thread-safe side effect container
 * It only returns each SideEffect once, if you use it as an [Iterable] it will emit each SideEffect and remove them so it's a perfect case for one shot SideEffects.
 * It locks itself, so you can't add and read at the same time, also it's not possible to read it at the same time from different threads, being completely thread-safe.
 */
public class SideEffects<T>() : Iterable<T> {

    private val sideEffects: Queue<T> = LinkedList()

    private constructor(sideEffects: Iterable<T>) : this() {
        this.sideEffects.addAll(sideEffects)
    }

    public fun add(vararg sideEffectsToAdd: T): SideEffects<T> {
        return SideEffects(sideEffects + sideEffectsToAdd)
    }

    public fun clear(): SideEffects<T> {
        return SideEffects()
    }

    override fun iterator(): Iterator<T> = iterator {
        do {
            val nextSideEffect: T? = sideEffects.poll()
            nextSideEffect?.let { yield(it) }
        } while (nextSideEffect != null)
    }
}
