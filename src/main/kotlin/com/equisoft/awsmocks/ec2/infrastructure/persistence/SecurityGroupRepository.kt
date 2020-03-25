package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.SecurityGroup

class SecurityGroupRepository : BaseEc2Repository<String, SecurityGroup>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<SecurityGroup> = listOf()
}
