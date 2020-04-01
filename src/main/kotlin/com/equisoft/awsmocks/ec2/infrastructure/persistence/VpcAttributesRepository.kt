package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.VpcAttributeName
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class VpcAttributesRepository : ConcurrentMap<String, Set<VpcAttributeName>> by ConcurrentHashMap() {
    @Synchronized
    fun add(vpcId: String, value: VpcAttributeName) {
        val attributes: Set<VpcAttributeName> = getOrPut(vpcId, { setOf() })
        put(vpcId, attributes + value)
    }

    @Synchronized
    fun remove(vpcId: String, value: VpcAttributeName) {
        val attributes: Set<VpcAttributeName> = getOrPut(vpcId, { setOf() })
        put(vpcId, attributes - value)
    }
}
