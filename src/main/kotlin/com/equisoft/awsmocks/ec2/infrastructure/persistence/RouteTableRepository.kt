package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.RouteTable
import com.equisoft.awsmocks.ec2.domain.applyFilters

class RouteTableRepository : BaseEc2Repository<String, RouteTable>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<RouteTable> = values.toList().applyFilters(filters)
}
