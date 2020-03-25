package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.equisoft.awsmocks.common.infrastructure.persistence.Repository

interface Ec2Repository<K, V> : Repository<K, V> {
    fun find(filters: List<Filter>): List<V>
}
