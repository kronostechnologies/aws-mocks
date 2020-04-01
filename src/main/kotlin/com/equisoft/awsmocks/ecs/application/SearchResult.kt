package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.Failure

class SearchResult<T> {
    private val found: MutableList<T> = mutableListOf()
    private val failures: MutableList<Failure> = mutableListOf()

    fun getFound(): List<T> = found.toList()
    fun getFailures(): List<Failure> = failures.toList()

    fun withFound(vararg found: T): SearchResult<T> {
        this.found.addAll(found)
        return this
    }

    fun withFound(found: Collection<T>): SearchResult<T> {
        this.found.addAll(found)
        return this
    }

    fun withFailures(vararg failures: Failure): SearchResult<T> {
        this.failures.addAll(failures)
        return this
    }

    fun withFailures(failures: Collection<Failure>): SearchResult<T> {
        this.failures.addAll(failures)
        return this
    }
}
