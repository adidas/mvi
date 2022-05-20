package com.adidas.mvi.actions

import java.util.LinkedList
import java.util.Queue

/**
 * A thread-safe action container
 * It only returns each actions once, if you use it as an [Iterable] it will emit each action and remove them so it's a perfect case for one shot actions
 * It locks itself so you can't add and read at the same time, also it's not possible to read it at the same time from different threads, being completely thread-safe
 */
public class Actions<T>() : Iterable<T> {

    private val actions: Queue<T> = LinkedList()

    private constructor(actions: Iterable<T>) : this() {
        this.actions.addAll(actions)
    }

    public fun add(vararg actionsToAdd: T): Actions<T> {
        return Actions(actions + actionsToAdd)
    }

    public fun clear(): Actions<T> {
        return Actions()
    }

    override fun iterator(): Iterator<T> = iterator<T> {
        do {
            val nextAction: T? = actions.poll()
            nextAction?.let { yield(it) }
        } while (nextAction != null)
    }
}
