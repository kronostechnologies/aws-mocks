package com.equisoft.awsmocks.route53.infrastructure.persistence

import com.amazonaws.services.route53.model.VPC
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class VpcAssociationRepository : ConcurrentMap<String, List<VPC>> by ConcurrentHashMap() {
    @Synchronized
    fun addOrUpdate(key: String, value: VPC) {
        val vpcs = getOrPut(key, { listOf() })
            .filter { it.vpcId != value.vpcId }

        put(key, vpcs + value)
    }

    @Synchronized
    fun dissociate(key: String, value: VPC) {
        val vpcs = getOrPut(key, { listOf() })
            .filter { it.vpcId == value.vpcId && it.vpcRegion == value.vpcRegion }

        put(key, vpcs)
    }
}
