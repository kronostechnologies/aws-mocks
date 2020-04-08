package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.PrefixList
import com.amazonaws.services.ec2.model.SecurityGroupIdentifier
import com.amazonaws.services.ec2.model.ServiceDetail
import com.amazonaws.services.ec2.model.Tag
import com.amazonaws.services.ec2.model.VpcEndpoint
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.ServiceDetailRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcEndpointRepository

class VpcEndpointService(
    private val serviceDetailRepository: ServiceDetailRepository,
    vpcEndpointRepository: VpcEndpointRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<VpcEndpoint, VpcEndpointRepository>({ it.vpcEndpointId }, vpcEndpointRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidVpcEndpointId.NotFound"

    override fun addOrUpdate(value: VpcEndpoint) {
        super.addOrUpdate(value)

        tagsRepository.update(value.vpcEndpointId, value.tags)
    }

    fun getServiceDetails(serviceNames: List<String>?): List<ServiceDetail> =
        serviceNames?.mapNotNull { serviceDetailRepository[it] } ?: serviceDetailRepository.getAll()

    fun getPrefixLists(filters: List<Filter>): List<PrefixList> {
        val prefixList = PrefixList().withCidrs("10.0.0.1/16")

        filters.forEach {
            when (it.name) {
                "prefix-list-id" -> prefixList.withPrefixListId(it.values.firstOrNull())
                "prefix-list-name" -> prefixList.withPrefixListName(it.values.firstOrNull())
            }
        }

        return listOf(prefixList.apply {
            if (prefixListId == null && prefixListName != null) {
                withPrefixListId(prefixListName)
            } else if (prefixListId != null && prefixListName == null) {
                withPrefixListName(prefixListId)
            }
        })
    }

    override fun withTags(value: VpcEndpoint, tags: Collection<Tag>): VpcEndpoint = value.withTags(tags)

    fun updatePolicyDocument(vpcEndpointId: String, policyDocument: String?) {
        val vpcEndpoint: VpcEndpoint = get(vpcEndpointId)
        vpcEndpoint.policyDocument = policyDocument
    }

    fun updateRouteTable(vpcEndpointId: String, toAdd: List<String>, toRemove: List<String>) {
        val vpcEndpoint: VpcEndpoint = get(vpcEndpointId)
        vpcEndpoint.setRouteTableIds(vpcEndpoint.routeTableIds - toRemove + toAdd)
    }

    fun updateSecurityGroups(vpcEndpointId: String, toAdd: List<String>, toRemove: List<String>) {
        val vpcEndpoint: VpcEndpoint = get(vpcEndpointId)
        vpcEndpoint.setGroups(vpcEndpoint.groups.filterNot { it.groupId in toRemove } + toAdd.map {
            SecurityGroupIdentifier().withGroupId(it).withGroupName(it)
        })
    }

    fun updateSubnets(vpcEndpointId: String, toAdd: List<String>, toRemove: List<String>) {
        val vpcEndpoint: VpcEndpoint = get(vpcEndpointId)
        vpcEndpoint.setSubnetIds(vpcEndpoint.routeTableIds - toRemove + toAdd)
    }

    @Synchronized
    @SuppressWarnings("LongParameterList")
    fun update(
        vpcEndpointId: String,
        policyDocument: String?,
        addRouteTableIds: List<String>,
        removeRouteTableIds: List<String>,
        addSecurityGroupIds: List<String>,
        removeSecurityGroupIds: List<String>,
        addSubnetIds: List<String>,
        removeSubnetIds: List<String>
    ) {
        updatePolicyDocument(vpcEndpointId, policyDocument)
        updateRouteTable(vpcEndpointId, addRouteTableIds, removeRouteTableIds)
        updateSecurityGroups(vpcEndpointId, addSecurityGroupIds, removeSecurityGroupIds)
        updateSubnets(vpcEndpointId, addSubnetIds, removeSubnetIds)
    }
}
