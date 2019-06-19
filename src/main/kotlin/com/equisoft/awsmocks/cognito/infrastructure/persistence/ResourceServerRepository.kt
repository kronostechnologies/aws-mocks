package com.equisoft.awsmocks.cognito.infrastructure.persistence

import com.amazonaws.services.cognitoidp.model.ResourceServerType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ResourceServerRepository : ConcurrentMap<String, List<ResourceServerType>> by ConcurrentHashMap() {
    fun add(key: String, value: ResourceServerType) {
        val clients: List<ResourceServerType> = getOrPut(key, { listOf() })
            .filter { it.identifier != value.identifier }
        put(key, clients + value)
    }

    fun find(userPoolId: String, serverId: String): ResourceServerType? =
        get(userPoolId)?.find { it.identifier == serverId }
}
