package com.adidas.mvi.sideeffects

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/**
 * A thread-safe side effect container
 * It only returns each SideEffect once, if you use it as an [Iterable] it will emit each SideEffect and remove them so it's a perfect case for one shot SideEffects.
 * It locks itself, so you can't add and read at the same time, also it's not possible to read it at the same time from different threads, being completely thread-safe.
 */

public class SideEffects<T>() : Iterable<T> {
    private val sideEffects: AtomicRef<MutableList<T>> = atomic(ArrayList())

    // Private constructor to initialize from an Iterable
    private constructor(sideEffects: Iterable<T>) : this() {
        this.sideEffects.value.addAll(sideEffects)
    }

    public fun add(vararg sideEffectsToAdd: T): SideEffects<T> {
        val newList = sideEffects.value.toMutableList()
        newList.addAll(sideEffectsToAdd)
        return SideEffects(newList)
    }

    public fun clear(): SideEffects<T> {
        return SideEffects()
    }

    override fun iterator(): Iterator<T> = iterator {
        while (true) {
            val currentList = sideEffects.value
            if (currentList.isEmpty()) break
            val nextSideEffect = currentList.removeFirstOrNull()
            nextSideEffect?.let { yield(it) }
        }
    }
}