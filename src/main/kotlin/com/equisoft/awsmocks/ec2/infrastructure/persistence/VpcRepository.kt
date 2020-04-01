package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Vpc

class VpcRepository : BaseEc2Repository<String, Vpc>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<Vpc> = listOf()
}
