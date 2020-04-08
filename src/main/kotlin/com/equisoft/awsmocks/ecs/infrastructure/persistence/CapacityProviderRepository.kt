package com.equisoft.awsmocks.ecs.infrastructure.persistence

import com.amazonaws.services.ecs.model.CapacityProvider
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class CapacityProviderRepository : ConcurrentMap<String, CapacityProvider> by ConcurrentHashMap() {
    @Synchronized
    fun findByArn(arn: String): CapacityProvider? = values.find { it.capacityProviderArn == arn }
}
