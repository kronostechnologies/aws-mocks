package com.equisoft.awsmocks.common.infrastructure.persistence

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ResourceTagsRepository<T>(private val getValue: T.() -> String) :
    ConcurrentMap<String, List<T>> by ConcurrentHashMap() {

    @Synchronized
    fun update(key: String, toAdd: List<T>, toRemove: List<String> = listOf()) {
        val tags: List<T> = getOrPut(key, { listOf() }).filterNot { it.getValue() in toRemove } + toAdd
        put(key, tags)
    }
}
