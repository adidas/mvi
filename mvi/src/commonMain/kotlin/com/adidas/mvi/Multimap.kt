package com.adidas.mvi

import kotlin.reflect.KClass

internal class Multimap<TKey : Any, TValue> {
    private val innerMap = hashMapOf<TKey, MutableList<MultimapEntry<TKey, TValue>>>()

    val keys: Collection<TKey>
        get() = innerMap.keys.toSet()

    fun put(
        key: TKey,
        value: TValue,
    ): MultimapEntry<TKey, TValue> {
        val entry = MultimapEntry(key, value)

        innerMap.getOrPut(key) {
            mutableListOf()
        }.add(entry)

        return entry
    }

    private fun putAllReplacing(
        key: TKey,
        values: List<MultimapEntry<TKey, TValue>>,
    ) {
        innerMap[key] = MutableList(values.size) { values[it] }
    }

    operator fun get(key: TKey): List<MultimapEntry<TKey, TValue>> = innerMap[key]?.toList() ?: listOf()

    operator fun <T : TKey> get(keyClass: KClass<T>): List<MultimapEntry<TKey, TValue>> =
        keys
            .filter { keyClass.isInstance(it) }
            .flatMap { get(it) }

    fun remove(multimapEntry: MultimapEntry<TKey, TValue>) {
        val key = multimapEntry.key
        val list = innerMap[key]

        list?.let {
            it.remove(multimapEntry)
            if (it.isEmpty()) {
                innerMap.remove(key)
            }
        }
    }

    fun copy(): Multimap<TKey, TValue> {
        val newMultimap = Multimap<TKey, TValue>()

        innerMap.forEach { (key, mutableList) ->
            newMultimap.putAllReplacing(key, mutableList)
        }

        return newMultimap
    }
}

internal data class MultimapEntry<TKey, TValue>(val key: TKey, val value: TValue)
