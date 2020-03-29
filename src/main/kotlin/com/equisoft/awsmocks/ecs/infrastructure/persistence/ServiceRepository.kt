package com.equisoft.awsmocks.ecs.infrastructure.persistence

import com.amazonaws.services.ecs.model.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ServiceRepository : ConcurrentMap<String, Service> by ConcurrentHashMap() {
    @Synchronized
    fun findByArn(arn: String): Service? = values.find { it.serviceArn == arn }

    @Synchronized
    fun findByClusterArn(clusterArn: String, serviceArns: List<String>): List<Service> = values.filter {
        it.clusterArn == clusterArn && serviceArns.contains(it.serviceArn)
    }
}
