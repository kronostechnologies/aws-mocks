package com.equisoft.awsmocks.ec2.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ec2.model.*
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.EC2
import com.equisoft.awsmocks.common.infrastructure.aws.readResource
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.domain.InstanceStateCodes.Companion.RUNNING
import com.equisoft.awsmocks.ec2.domain.InstanceStateCodes.Companion.SHUTTING_DOWN
import com.equisoft.awsmocks.ec2.domain.filter
import com.equisoft.awsmocks.ec2.domain.find
import com.equisoft.awsmocks.ec2.domain.findResourceType
import com.equisoft.awsmocks.ec2.infrastructure.persistence.ResourcesRepository
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.DescribeInstanceAttributeResponse
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.RunInstancesResponse
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.TerminateInstancesResponse

@SuppressWarnings("LongMethod")
class Ec2RequestHandler(
    private val instanceService: InstanceService,
    private val internetGatewayService: InternetGatewayService,
    private val reservationService: ReservationService,
    private val routeTableService: RouteTableService,
    private val securityGroupService: SecurityGroupService,
    private val subnetService: SubnetService,
    private val tagsRepository: ResourceTagsRepository<Tag>,
    private val volumeService: VolumeService,
    private val vpcService: VpcService,
    private val vpcEndpointService: VpcEndpointService,
    private val resourcesRepository: ResourcesRepository
) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> =
        when (request) {
            is AssociateRouteTableRequest -> {
                val routeTableAssociation: RouteTableAssociation = routeAssociationFromRequest(request)
                routeTableService.addAssociation(routeTableAssociation)

                AssociateRouteTableResult().withAssociationId(routeTableAssociation.routeTableAssociationId)
                    .withAssociationState(routeTableAssociation.associationState)
            }
            is AttachInternetGatewayRequest -> {
                internetGatewayService.attach(request.internetGatewayId, request.vpcId)
                AttachInternetGatewayResult()
            }
            is AuthorizeSecurityGroupEgressRequest -> {
                securityGroupService.authorizeEgress(request.groupId, request.ipPermissions)

                AuthorizeSecurityGroupEgressResult()
            }
            is AuthorizeSecurityGroupIngressRequest -> {
                securityGroupService.authorizeIngress(request.groupId, request.ipPermissions)

                AuthorizeSecurityGroupIngressResult()
            }
            is CreateInternetGatewayRequest -> {
                val internetGateway: InternetGateway = internetGatewayFromRequest(request)
                internetGatewayService.addOrUpdate(internetGateway)

                CreateInternetGatewayResult().withInternetGateway(internetGateway)
            }
            is CreateRouteRequest -> {
                val route: Route = routeFromRequest(request)
                routeTableService.addRoute(request.routeTableId, route)

                CreateRouteResult().withReturn(true)
            }
            is CreateRouteTableRequest -> {
                val routeTable: RouteTable = routeTableFromRequest(request)
                routeTableService.addOrUpdate(routeTable)

                CreateRouteTableResult().withRouteTable(routeTable)
            }
            is CreateSecurityGroupRequest -> {
                val securityGroup: SecurityGroup = securityGroupFromRequest(request)
                securityGroupService.addOrUpdate(securityGroup)

                CreateSecurityGroupResult().withGroupId(securityGroup.groupId)
            }
            is CreateSubnetRequest -> {
                val subnet: Subnet = subnetFromRequest(request).apply { requireNotNull(vpcId) }
                subnetService.addOrUpdate(subnet)

                CreateSubnetResult().withSubnet(subnet)
            }
            is CreateTagsRequest -> {
                request.resources.forEach { id ->
                    tagsRepository.update(id, request.tags)
                }

                CreateTagsResult()
            }
            is CreateVpcEndpointRequest -> {
                val vpcEndpoint: VpcEndpoint = vpcEndpointFromRequest(request)
                vpcEndpointService.addOrUpdate(vpcEndpoint)

                CreateVpcEndpointResult().withClientToken(request.clientToken).withVpcEndpoint(vpcEndpoint)
            }
            is CreateVpcRequest -> {
                val vpc: Vpc = vpcFromRequest(request)
                vpcService.addOrUpdate(vpc)

                CreateVpcResult().withVpc(vpc)
            }
            is DescribeAccountAttributesRequest -> {
                val attribute: AccountAttribute = AccountAttribute()
                    .withAttributeName("supported-platforms")
                    .withAttributeValues(AccountAttributeValue().withAttributeValue("VPC"))

                DescribeAccountAttributesResult().withAccountAttributes(attribute)
            }
            is DescribeImagesRequest -> {
                readResource<DescribeImagesResult>(EC2).filter(request.filters)
            }
            is DescribeInstanceAttributeRequest -> {
                val instance: Instance = instanceService.get(request.instanceId)
                val instanceAttribute = InstanceAttribute().withInstanceId(instance.instanceId)
                    .withInstanceType(instance.instanceType)
                    .withBlockDeviceMappings(instance.blockDeviceMappings)
                    .withDisableApiTermination(false)
                    .withEbsOptimized(instance.ebsOptimized)
                    .withEnaSupport(instance.enaSupport)
                    .withGroups(instance.securityGroups)
                    .withKernelId(instance.kernelId)
                    .withRamdiskId(instance.ramdiskId)
                    .withRootDeviceName(instance.rootDeviceName)
                    .withSourceDestCheck(instance.sourceDestCheck)
                    .withSriovNetSupport(instance.sriovNetSupport)

                DescribeInstanceAttributeResponse(instanceAttribute)
            }
            is DescribeInstanceCreditSpecificationsRequest -> {
                DescribeInstanceCreditSpecificationsResult().withInstanceCreditSpecifications(request.instanceIds.map {
                    InstanceCreditSpecification().withInstanceId(it).withCpuCredits("unlimited")
                })
            }
            is DescribeInstancesRequest -> {
                val reservations: List<Reservation> = reservationService.getAll(filters = request.filters)

                DescribeInstancesResult().withReservations(reservations)
            }
            is DescribeInternetGatewaysRequest -> {
                val internetGateways = internetGatewayService.getAll(request.internetGatewayIds, request.filters)

                DescribeInternetGatewaysResult().withInternetGateways(internetGateways)
            }
            is DescribeNetworkAclsRequest -> {
                DescribeNetworkAclsResult()
            }
            is DescribePrefixListsRequest -> {
                val prefixLists: List<PrefixList> = vpcEndpointService.getPrefixLists(request.filters)

                DescribePrefixListsResult().withPrefixLists(prefixLists)
            }
            is DescribeRouteTablesRequest -> {
                val routeTables: List<RouteTable> = routeTableService.getAll(
                    ids = request.routeTableIds,
                    filters = request.filters
                )

                DescribeRouteTablesResult().withRouteTables(routeTables)
            }
            is DescribeSecurityGroupsRequest -> {
                val securityGroups: List<SecurityGroup> = securityGroupService.getAll(request.filters, request.groupIds,
                    request.groupNames)

                DescribeSecurityGroupsResult().withSecurityGroups(securityGroups)
            }
            is DescribeSubnetsRequest -> {
                val subnets: List<Subnet> = subnetService.getAll(
                    ids = request.subnetIds,
                    filters = request.filters
                )

                DescribeSubnetsResult().withSubnets(subnets)
            }
            is DescribeTagsRequest -> {
                val tags: List<TagDescription>? = request.filters.find("resource-id")
                    ?.let { filter ->
                        val resourceId = filter.values.firstOrNull()
                        val resource = resourcesRepository.findResource(resourceId)
                        tagsRepository[resourceId]?.map {
                            TagDescription().withKey(it.key)
                                .withValue(it.value)
                                .withResourceId(resourceId)
                                .withResourceType(findResourceType(resource))
                        }
                    }

                DescribeTagsResult().withTags(tags)
            }
            is DescribeVolumesRequest -> {
                val volumes: List<Volume> = volumeService.getAll(request.volumeIds, request.filters)

                DescribeVolumesResult().withVolumes(volumes)
            }
            is DescribeVpcAttributeRequest -> {
                val vpcAttribute: VpcAttributeName = VpcAttributeName.fromValue(request.attribute)
                val hasAttribute: Boolean = vpcService.hasAttribute(request.vpcId, vpcAttribute)

                val isAttributeActive: (attribute: VpcAttributeName) -> Boolean? = {
                    if (vpcAttribute == it) hasAttribute else null
                }

                DescribeVpcAttributeResult()
                    .withVpcId(request.vpcId)
                    .withEnableDnsHostnames(isAttributeActive(VpcAttributeName.EnableDnsHostnames))
                    .withEnableDnsSupport(isAttributeActive(VpcAttributeName.EnableDnsSupport))
            }
            is DescribeVpcClassicLinkRequest -> {
                // TODO(meriouma): Incomplete for now, just needed an empty answer
                DescribeVpcClassicLinkResult()
            }
            is DescribeVpcClassicLinkDnsSupportRequest -> {
                // TODO(meriouma): Incomplete for now, just needed an empty answer
                DescribeVpcClassicLinkDnsSupportResult()
            }
            is DescribeVpcEndpointsRequest -> {
                val vpcEndpoints: List<VpcEndpoint> = vpcEndpointService.getAll(request.vpcEndpointIds)

                DescribeVpcEndpointsResult().withVpcEndpoints(vpcEndpoints)
            }
            is DescribeVpcEndpointServicesRequest -> {
                val serviceDetails: List<ServiceDetail> = vpcEndpointService.getServiceDetails(request.serviceNames)

                DescribeVpcEndpointServicesResult()
                    .withServiceDetails(serviceDetails)
                    .withServiceNames(serviceDetails.map { it.serviceName })
            }
            is DescribeVpcsRequest -> {
                val vpcs: List<Vpc> = vpcService.getAll(request.vpcIds, request.filters)

                DescribeVpcsResult().withVpcs(vpcs)
            }
            is DisassociateRouteTableRequest -> {
                routeTableService.removeAssociation(request.associationId)

                DisassociateRouteTableResult()
            }
            is ModifyInstanceAttributeRequest -> {
                instanceService.modifyAttributes(request.instanceId, request)

                ModifyInstanceAttributeResult()
            }
            is ModifyVpcAttributeRequest -> {
                vpcService.modifyAttributes(request.vpcId,
                    VpcAttributeName.EnableDnsHostnames to request.enableDnsHostnames,
                    VpcAttributeName.EnableDnsSupport to request.enableDnsSupport
                )

                ModifyVpcAttributeResult()
            }
            is ModifyVpcEndpointRequest -> {
                vpcEndpointService.updatePolicyDocument(request.vpcEndpointId, request.policyDocument)
                vpcEndpointService.updateRouteTable(request.vpcEndpointId, request.addRouteTableIds,
                    request.removeRouteTableIds)
                vpcEndpointService.updateSecurityGroups(request.vpcEndpointId, request.addSecurityGroupIds,
                    request.removeSecurityGroupIds)
                vpcEndpointService.updateSubnets(request.vpcEndpointId, request.addSubnetIds, request.removeSubnetIds)

                ModifyVpcEndpointResult().withReturn(true)
            }
            is RevokeSecurityGroupEgressRequest -> {
                securityGroupService.revokeEgress(request.groupId, request.ipPermissions)

                RevokeSecurityGroupEgressResult()
            }
            is RunInstancesRequest -> {
                val reservation: Reservation = instancesReservationFromRequest(request)
                reservationService.addOrUpdate(reservation)

                RunInstancesResponse(reservation)
            }
            is TerminateInstancesRequest -> {
                instanceService.terminateInstances(request.instanceIds)

                TerminateInstancesResponse(request.instanceIds.map {
                    InstanceStateChange().withInstanceId(it)
                        .withCurrentState(InstanceState().withCode(SHUTTING_DOWN).withName("shutting-down"))
                        .withPreviousState(InstanceState().withCode(RUNNING).withName("running"))
                })
            }
            else -> throw IllegalArgumentException(request::class.qualifiedName)
        }
}
