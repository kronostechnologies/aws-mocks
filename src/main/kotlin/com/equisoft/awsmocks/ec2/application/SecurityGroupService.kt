package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.SecurityGroupRepository

class SecurityGroupService(
    securityGroupRepository: SecurityGroupRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<SecurityGroup, SecurityGroupRepository>({ it.groupId }, securityGroupRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidSecurityGroupID.NotFound"

    override fun withTags(value: SecurityGroup, tags: Collection<Tag>): SecurityGroup = value.withTags(tags)

    fun getAll(filters: List<Filter>, groupIds: List<String>, groupNames: List<String>): List<SecurityGroup> {
        val filteredGroups: MutableList<SecurityGroup> = mutableListOf()
        filteredGroups.addAll(super.getAll(groupIds, filters))

        if (groupNames.isNotEmpty()) {
            filteredGroups.addAll(
                repository.values.filter { it.groupName in groupNames }
                    .map { it.retrieveTags() }
            )
        }

        return filteredGroups
    }

    fun authorizeEgress(groupId: String, ipPermissions: List<IpPermission>) {
        val securityGroup: SecurityGroup = get(groupId)

        securityGroup.ipPermissionsEgress.addAll(ipPermissions)

        addOrUpdate(securityGroup)
    }

    fun authorizeIngress(groupId: String, ipPermissions: List<IpPermission>) {
        val securityGroup: SecurityGroup = get(groupId)

        securityGroup.ipPermissions.addAll(ipPermissions)

        addOrUpdate(securityGroup)
    }

    fun revokeEgress(groupId: String, ipPermissions: List<IpPermission>) {
        val securityGroup: SecurityGroup = get(groupId)

        securityGroup.ipPermissionsEgress.removeIf(ipPermissions::contains)

        addOrUpdate(securityGroup)
    }
}
