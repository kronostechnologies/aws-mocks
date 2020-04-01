package com.equisoft.awsmocks.route53.infrastructure.persistence

import com.amazonaws.services.route53.model.HostedZone
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DelegationSetAssociationRepository : ConcurrentMap<String, List<HostedZone>> by ConcurrentHashMap() {
    @Synchronized
    fun addOrUpdate(key: String, value: HostedZone) {
        val hostedZones = getOrPut(key, { listOf() })
            .filter { it.name != value.name }

        put(key, hostedZones + value)
    }

    @Synchronized
    fun removeHostedZone(hostedZone: HostedZone) = entries.forEach { (id, zones) ->
        put(id, zones - hostedZone)
    }

    @Synchronized
    fun find(hostedZone: HostedZone): String? = entries.find { it.value.contains(hostedZone) }?.key
}
