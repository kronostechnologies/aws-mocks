package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.equisoft.awsmocks.common.exceptions.BadRequestException
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.infrastructure.persistence.Repository
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository

abstract class BaseElbService<V : Any, R : Repository<String, V>>(
    private val idProvider: (V) -> String,
    protected val repository: R,
    protected val tagsRepository: ResourceTagsRepository<Tag>
) {
    protected abstract val duplicateMessage: String
    protected abstract val notFoundMessage: String

    fun add(value: V) {
        val id = idProvider(value)
        if (repository[id] != null) {
            throw BadRequestException(duplicateMessage)
        }

        repository[id] = value
    }

    fun get(id: String): V = repository[id] ?: throw NotFoundException(notFoundMessage)

    fun getAll(ids: List<String>): List<V> = ids.map(::get)

    fun find(id: String): V? = repository[id]

    fun remove(name: String) {
        repository.remove(name)
    }

    fun addTagsByArn(arn: String, tags: List<Tag>) {
        getByArn(arn)

        tagsRepository.update(arn, tags)
    }

    fun getTagsByArn(arn: String): List<Tag> {
        getByArn(arn)

        return tagsRepository[arn] ?: listOf()
    }

    fun getByArn(arn: String): V = repository.values.find { getArn(it) == arn }
        ?: throw NotFoundException(notFoundMessage)

    protected abstract fun getArn(value: V): String
}
