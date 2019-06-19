package com.equisoft.awsmocks.cognito.infrastructure.persistence

import com.amazonaws.services.cognitoidp.model.DomainDescriptionType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class UserPoolDomainRepository : ConcurrentMap<String, List<DomainDescriptionType>> by ConcurrentHashMap() {
    fun add(key: String, value: DomainDescriptionType) {
        val clients: List<DomainDescriptionType> = getOrPut(key, { listOf() })
            .filter { it.domain != value.domain }
        put(key, clients + value)
    }

    fun find(domain: String): DomainDescriptionType? = values.flatten().find { it.domain == domain }
}
