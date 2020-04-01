package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.equisoft.awsmocks.common.infrastructure.persistence.Repository

class ResourcesRepository(private val repositories: List<Repository<String, *>>) {
    @Synchronized
    fun findResource(id: String?): Any? = id?.let {
        repositories.find { repo -> repo.containsKey(id) }?.get(id)
    }
}
