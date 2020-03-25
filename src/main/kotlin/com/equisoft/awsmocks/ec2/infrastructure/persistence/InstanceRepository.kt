package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Instance

class InstanceRepository : BaseEc2Repository<String, Instance>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<Instance> = listOf()
}
