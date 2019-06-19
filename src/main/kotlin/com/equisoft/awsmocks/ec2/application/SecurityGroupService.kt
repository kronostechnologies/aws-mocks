package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.SecurityGroup
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.ec2.infrastructure.persistence.SecurityGroupRepository

class SecurityGroupService(
    private val securityGroupRepository: SecurityGroupRepository
) {
    fun addOrUpdate(securityGroup: SecurityGroup) {
        securityGroupRepository[securityGroup.groupId] = securityGroup
    }

    fun get(id: String): SecurityGroup = securityGroupRepository[id]
        ?: throw NotFoundException("InvalidSecurityGroupId.NotFound")

    fun getAll(filters: List<Filter>, groupIds: List<String>, groupNames: List<String>): List<SecurityGroup> {
        val groups: MutableCollection<SecurityGroup> = securityGroupRepository.values

        return when {
            filters.isNotEmpty() -> groups.toList() // Not used at the moment
            groupIds.isNotEmpty() -> groups.filter { it.groupId in groupIds }
            groupNames.isNotEmpty() -> groups.filter { it.groupName in groupNames }
            else -> groups.toList()
        }
    }
}
