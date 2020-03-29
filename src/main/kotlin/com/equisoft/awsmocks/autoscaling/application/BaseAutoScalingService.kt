package com.equisoft.awsmocks.autoscaling.application

import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.exceptions.BadRequestException
import com.equisoft.awsmocks.common.infrastructure.persistence.Repository
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository

@SuppressWarnings("UnnecessaryAbstractClass")
abstract class BaseAutoScalingService<V : Any, R : Repository<String, V>>(
    private val idProvider: (V) -> String,
    protected val repository: R,
    protected val tagsRepository: ResourceTagsRepository<Tag>
) {
    fun add(value: V) {
        val id = idProvider(value)
        if (repository[id] != null) {
            throw BadRequestException("AlreadyExists")
        }

        repository[id] = value
    }

    fun update(value: V) {
        repository[idProvider(value)] = value
    }

    fun getAll(ids: List<String>): List<V> = ids.mapNotNull(repository::get)

    fun find(id: String): V? = repository[id]

    fun remove(name: String) {
        repository.remove(name)
    }
}
