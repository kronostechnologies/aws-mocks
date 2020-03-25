package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.VpcEndpoint

class VpcEndpointRepository : BaseEc2Repository<String, VpcEndpoint>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<VpcEndpoint> = listOf()
}
