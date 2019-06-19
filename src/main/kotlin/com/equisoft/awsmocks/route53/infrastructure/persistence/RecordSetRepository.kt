package com.equisoft.awsmocks.route53.infrastructure.persistence

import com.amazonaws.services.route53.model.ResourceRecordSet
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class RecordSetRepository : ConcurrentMap<String, List<ResourceRecordSet>> by ConcurrentHashMap() {
    fun addOrUpdate(key: String, value: ResourceRecordSet) {
        val records = getOrPut(key, { listOf() })
            .filter { it.name != value.name }

        put(key, records + value)
    }

    fun remove(key: String, value: ResourceRecordSet) {
        val records = get(key)
        if (records != null) {
            put(key, records.filter { it.name == value.name })
        }
    }
}
