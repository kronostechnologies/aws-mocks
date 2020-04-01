package com.equisoft.awsmocks.autoscaling.infrastructure.persistence

import com.equisoft.awsmocks.common.infrastructure.persistence.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

abstract class BaseAutoScalingRepository<K, V> : Repository<K, V>, ConcurrentMap<K, V> by ConcurrentHashMap() {
    @Synchronized
    fun getAll(): List<V> = values.toList()
}
