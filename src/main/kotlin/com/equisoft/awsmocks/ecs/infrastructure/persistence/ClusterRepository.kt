package com.equisoft.awsmocks.ecs.infrastructure.persistence

import com.amazonaws.services.ecs.model.Cluster
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ClusterRepository : ConcurrentMap<String, Cluster> by ConcurrentHashMap() {
    @Synchronized
    fun findByArn(arn: String): Cluster? = values.find { it.clusterArn == arn }
}
