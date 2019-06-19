package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.PrefixList
import com.amazonaws.services.ec2.model.SecurityGroupIdentifier
import com.amazonaws.services.ec2.model.ServiceDetail
import com.amazonaws.services.ec2.model.VpcEndpoint
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.ec2.infrastructure.persistence.ServiceDetailRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcEndpointRepository

class VpcService(
    private val serviceDetailRepository: ServiceDetailRepository,
    private val vpcEndpointRepository: VpcEndpointRepository
) {
    fun addOrUpdate(vpcEndpoint: VpcEndpoint) {
        vpcEndpointRepository[vpcEndpoint.vpcEndpointId] = vpcEndpoint
    }

    fun get(id: String): VpcEndpoint = vpcEndpointRepository[id]
        ?: throw NotFoundException("InvalidVpcEndpointId.NotFound")

    fun getServiceDetails(serviceNames: List<String>?): List<ServiceDetail> =
        serviceNames?.mapNotNull { serviceDetailRepository[it] } ?: serviceDetailRepository.getAll()

    fun getAll(endpointIds: List<String>?): List<VpcEndpoint> {
        val values: MutableCollection<VpcEndpoint> = vpcEndpointRepository.values
        if (endpointIds == null || endpointIds.isEmpty()) {
            return values.toList()
        }

        return values.filter { it.vpcEndpointId in endpointIds }
    }

    fun getPrefixLists(filters: List<Filter>?): List<PrefixList> {
        val values: MutableCollection<VpcEndpoint> = vpcEndpointRepository.values
        if (filters == null || filters.isEmpty()) {
            return values.map(::createPrefixList)
        }

        return values.filter { vpcEndpoint ->
            filters.any { filter ->
                when (filter.name) {
                    "prefix-list-id" -> vpcEndpoint.vpcEndpointId in filter.values
                    "prefix-list-name" -> vpcEndpoint.serviceName in filter.values
                    else -> false
                }
            }
        }.map(::createPrefixList)
    }

    private fun createPrefixList(vpcEndpoint: VpcEndpoint): PrefixList = PrefixList()
        .withPrefixListId(vpcEndpoint.vpcEndpointId)
        .withPrefixListName(vpcEndpoint.serviceName)
        .withCidrs("10.0.0.1/16")

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
}
