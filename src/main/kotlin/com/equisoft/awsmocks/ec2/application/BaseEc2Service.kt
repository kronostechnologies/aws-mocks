package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.Ec2Repository

abstract class BaseEc2Service<V, R : Ec2Repository<String, V>>(
    private val idProvider: (V) -> String,
    protected val repository: R,
    protected val tagsRepository: ResourceTagsRepository<Tag>
) {
    open fun addOrUpdate(value: V) {
        repository[idProvider(value)] = value
    }

    open fun getAll(ids: List<String>? = null, filters: List<Filter>? = null): List<V> {
        val elements: MutableList<V> = mutableListOf()

        elements.addAll(ids?.map(this::get).orEmpty())
        filters?.run {
            elements.addAll(
                repository.find(this).map { it.retrieveTags() }
            )
        }

        if (ids == null && filters == null) {
            elements.addAll(repository.values)
        }

        return elements
    }

    fun get(id: String): V = getWithoutTags(id).retrieveTags()

    fun addTags(id: String, tags: List<Tag>) {
        val value: V = getWithoutTags(id)
        tagsRepository.update(idProvider(value), tags)
    }

    fun getTags(id: String): List<Tag> {
        val value: V = getWithoutTags(id)
        return tagsRepository.getOrDefault(idProvider(value), listOf())
    }

    protected fun getWithoutTags(id: String): V =
        repository[id] ?: throw NotFoundException(notFoundMessage)

    protected fun V.retrieveTags(): V {
        val tags: List<Tag> = tagsRepository.getOrDefault(idProvider(this), listOf())
        return withTags(this, tags)
    }

    protected abstract fun withTags(value: V, tags: Collection<Tag>): V

    protected abstract val notFoundMessage: String
}
