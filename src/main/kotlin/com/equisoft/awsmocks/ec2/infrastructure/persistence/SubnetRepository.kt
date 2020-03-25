package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Subnet
import com.equisoft.awsmocks.ec2.domain.applyFilters

class SubnetRepository : BaseEc2Repository<String, Subnet>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<Subnet> = values.toList().applyFilters(filters)
}
