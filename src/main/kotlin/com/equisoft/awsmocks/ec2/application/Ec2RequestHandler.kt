package com.equisoft.awsmocks.ec2.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ec2.model.AccountAttribute
import com.amazonaws.services.ec2.model.AccountAttributeValue
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupEgressRequest
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupEgressResult
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult
import com.amazonaws.services.ec2.model.CreateVpcEndpointRequest
import com.amazonaws.services.ec2.model.CreateVpcEndpointResult
import com.amazonaws.services.ec2.model.DescribeAccountAttributesRequest
import com.amazonaws.services.ec2.model.DescribeAccountAttributesResult
import com.amazonaws.services.ec2.model.DescribePrefixListsRequest
import com.amazonaws.services.ec2.model.DescribePrefixListsResult
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
import com.amazonaws.services.ec2.model.DescribeVpcEndpointServicesRequest
import com.amazonaws.services.ec2.model.DescribeVpcEndpointServicesResult
import com.amazonaws.services.ec2.model.DescribeVpcEndpointsRequest
import com.amazonaws.services.ec2.model.DescribeVpcEndpointsResult
import com.amazonaws.services.ec2.model.ModifyVpcEndpointRequest
import com.amazonaws.services.ec2.model.ModifyVpcEndpointResult
import com.amazonaws.services.ec2.model.PrefixList
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.ec2.model.ServiceDetail
import com.amazonaws.services.ec2.model.VpcEndpoint

@SuppressWarnings("LongMethod")
class Ec2RequestHandler(
    private val vpcService: VpcService,
    private val securityGroupService: SecurityGroupService
) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> =
        when (request) {
            is AuthorizeSecurityGroupEgressRequest -> {
                AuthorizeSecurityGroupEgressResult()
            }
            is CreateSecurityGroupRequest -> {
                val securityGroup: SecurityGroup = securityGroupFromRequest(request)
                securityGroupService.addOrUpdate(securityGroup)

                CreateSecurityGroupResult().withGroupId(securityGroup.groupId)
            }
            is CreateVpcEndpointRequest -> {
                val vpcEndpoint: VpcEndpoint = vpcEndpointFromRequest(request)
                vpcService.addOrUpdate(vpcEndpoint)

                CreateVpcEndpointResult().withClientToken(request.clientToken).withVpcEndpoint(vpcEndpoint)
            }
            is DescribeAccountAttributesRequest -> {
                val attribute: AccountAttribute = AccountAttribute()
                    .withAttributeName("supported-platforms")
                    .withAttributeValues(AccountAttributeValue().withAttributeValue("VPC"))

                DescribeAccountAttributesResult().withAccountAttributes(attribute)
            }
            is DescribePrefixListsRequest -> {
                val prefixLists: List<PrefixList> = vpcService.getPrefixLists(request.filters)

                DescribePrefixListsResult().withPrefixLists(prefixLists)
            }
            is DescribeSecurityGroupsRequest -> {
                val securityGroups: List<SecurityGroup> = securityGroupService.getAll(request.filters, request.groupIds,
                    request.groupNames)

                DescribeSecurityGroupsResult().withSecurityGroups(securityGroups)
            }
            is DescribeVpcEndpointsRequest -> {
                val vpcEndpoints: List<VpcEndpoint> = vpcService.getAll(request.vpcEndpointIds)

                DescribeVpcEndpointsResult().withVpcEndpoints(vpcEndpoints)
            }
            is DescribeVpcEndpointServicesRequest -> {
                val serviceDetails: List<ServiceDetail> = vpcService.getServiceDetails(request.serviceNames)

                DescribeVpcEndpointServicesResult()
                    .withServiceDetails(serviceDetails)
                    .withServiceNames(serviceDetails.map { it.serviceName })
            }
            is ModifyVpcEndpointRequest -> {
                vpcService.updatePolicyDocument(request.vpcEndpointId, request.policyDocument)
                vpcService.updateRouteTable(request.vpcEndpointId, request.addRouteTableIds,
                    request.removeRouteTableIds)
                vpcService.updateSecurityGroups(request.vpcEndpointId, request.addSecurityGroupIds,
                    request.removeSecurityGroupIds)
                vpcService.updateSubnets(request.vpcEndpointId, request.addSubnetIds, request.removeSubnetIds)

                ModifyVpcEndpointResult().withReturn(true)
            }
            else -> throw IllegalArgumentException(request::class.simpleName)
        }
}
