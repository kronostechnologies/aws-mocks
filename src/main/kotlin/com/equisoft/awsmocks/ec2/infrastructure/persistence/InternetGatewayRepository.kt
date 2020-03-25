package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.InternetGateway
import com.equisoft.awsmocks.ec2.domain.applyFilters

class InternetGatewayRepository : BaseEc2Repository<String, InternetGateway>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<InternetGateway> = values.toList().applyFilters(filters)
}
