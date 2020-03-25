package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.ServiceDetail
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ServiceDetailRepository : ConcurrentMap<String, ServiceDetail> by ConcurrentHashMap() {
    @Synchronized
    fun getAll(): List<ServiceDetail> = values.toList()
}
