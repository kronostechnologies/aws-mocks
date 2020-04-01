package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.equisoft.awsmocks.common.infrastructure.persistence.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

abstract class BaseElbRepository<K, V> : Repository<K, V>, ConcurrentMap<K, V> by ConcurrentHashMap() {
    @Synchronized
    fun getAll(): List<V> = values.toList()
}
