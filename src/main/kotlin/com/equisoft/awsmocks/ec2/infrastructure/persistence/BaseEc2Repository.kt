package com.equisoft.awsmocks.ec2.infrastructure.persistence

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

abstract class BaseEc2Repository<K, V> : Ec2Repository<K, V>, ConcurrentMap<K, V> by ConcurrentHashMap() {
    @Synchronized
    fun getAll(): List<V> = values.toList()
}
