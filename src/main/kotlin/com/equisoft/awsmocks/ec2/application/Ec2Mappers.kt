@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.*
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.EC2
import com.equisoft.awsmocks.common.interfaces.http.accountId
import com.equisoft.awsmocks.common.interfaces.http.region
import com.equisoft.awsmocks.common.utils.mapEach
import com.equisoft.awsmocks.common.utils.mapTrue
import java.util.Date
import java.util.UUID

fun vpcFromRequest(request: CreateVpcRequest): Vpc = Vpc()
    .withVpcId(UUID.randomUUID().toString())
    .withCidrBlock(request.cidrBlock)
    .withInstanceTenancy(request.instanceTenancy)
    .withIsDefault(false)
    .withState(VpcState.Available)

fun vpcEndpointFromRequest(request: CreateVpcEndpointRequest): VpcEndpoint = VpcEndpoint()
    .withVpcEndpointId(UUID.randomUUID().toString())
    .withVpcEndpointType(request.vpcEndpointType)
    .withCreationTimestamp(Date())
    .withVpcId(request.vpcId)
    .withPrivateDnsEnabled(request.privateDnsEnabled)
    .withPolicyDocument(request.policyDocument)
    .withServiceName(request.serviceName)
    .withRouteTableIds(request.routeTableIds)
    .withGroups()
    .withDnsEntries()
    .withNetworkInterfaceIds()
    .withSubnetIds(request.subnetIds)
    .withState(State.Available.toString().toLowerCase())
    .withTags(request.tagSpecifications.flatMap(TagSpecification::getTags))

fun securityGroupFromRequest(request: CreateSecurityGroupRequest): SecurityGroup = SecurityGroup()
    .withGroupId(UUID.randomUUID().toString())
    .withGroupName(request.groupName)
    .withDescription(request.description)
    .withVpcId(request.vpcId)
    .withOwnerId(request.accountId)

fun subnetFromRequest(request: CreateSubnetRequest): Subnet {
    val subnetId = UUID.randomUUID().toString()

    return Subnet()
        .withVpcId(request.vpcId)
        .withSubnetId(subnetId)
        .withSubnetArn(EC2.createArn(request.accountId, "subnet/subnet-$subnetId"))
        .withAvailabilityZone(request.availabilityZone)
        .withAvailabilityZoneId(request.availabilityZoneId)
        .withCidrBlock(request.cidrBlock)
        .withState(SubnetState.Available)
}

fun routeTableFromRequest(request: CreateRouteTableRequest): RouteTable = RouteTable()
    .withRouteTableId(UUID.randomUUID().toString())
    .withVpcId(request.vpcId)

fun internetGatewayFromRequest(request: CreateInternetGatewayRequest): InternetGateway = InternetGateway()
    .withInternetGatewayId(UUID.randomUUID().toString())
    .withOwnerId(request.accountId)

fun routeAssociationFromRequest(request: AssociateRouteTableRequest): RouteTableAssociation = RouteTableAssociation()
    .withRouteTableAssociationId(UUID.randomUUID().toString())
    .withAssociationState(RouteTableAssociationState().withState(RouteTableAssociationStateCode.Associated))
    .withGatewayId(request.gatewayId)
    .withRouteTableId(request.routeTableId)
    .withSubnetId(request.subnetId)

fun routeFromRequest(request: CreateRouteRequest): Route = Route()
    .withDestinationCidrBlock(request.destinationCidrBlock)
    .withDestinationIpv6CidrBlock(request.destinationIpv6CidrBlock)
    .withEgressOnlyInternetGatewayId(request.egressOnlyInternetGatewayId)
    .withGatewayId(request.gatewayId)
    .withInstanceId(request.instanceId)
    .withLocalGatewayId(request.localGatewayId)
    .withNatGatewayId(request.natGatewayId)
    .withNetworkInterfaceId(request.networkInterfaceId)
    .withOrigin(RouteOrigin.CreateRoute)
    .withState(RouteState.Active)
    .withTransitGatewayId(request.transitGatewayId)
    .withVpcPeeringConnectionId(request.vpcPeeringConnectionId)

private const val INSTANCE_RUNNING = 16

fun instancesReservationFromRequest(request: RunInstancesRequest): Reservation = Reservation()
    .withReservationId(UUID.randomUUID().toString())
    .withGroups(request.securityGroups.map { GroupIdentifier().withGroupId(it) })
    .withOwnerId(request.accountId)
    .withRequesterId(request.accountId)
    .withInstances(request.minCount.mapEach { mapInstance(request) })

private fun mapInstance(request: RunInstancesRequest): Instance = Instance()
    .withClientToken(request.clientToken)
    .withBlockDeviceMappings(request.blockDeviceMappings.map(::mapInstanceBlockDeviceMapping))
    .withEbsOptimized(request.ebsOptimized)
    .withCpuOptions(request.cpuOptions?.let {
        CpuOptions().withCoreCount(it.coreCount).withThreadsPerCore(it.threadsPerCore)
    })
    .withIamInstanceProfile(
        request.iamInstanceProfile.let { IamInstanceProfile().withId(it.name).withArn(it.arn) }
    )
    .withImageId(request.imageId)
    .withInstanceId(UUID.randomUUID().toString())
    .withInstanceType(request.instanceType)
    .withKernelId(request.kernelId)
    .withKeyName(request.keyName)
    .withLaunchTime(Date())
    .withMonitoring(Monitoring().withState(
        if (request.monitoring) MonitoringState.Enabled else MonitoringState.Disabled)
    )
    .withNetworkInterfaces(request.networkInterfaces.map { mapInstanceNetworkInterface(it, request) })
    .withPlacement(request.placement)
    .withPrivateIpAddress(request.privateIpAddress)
    .withSecurityGroups(request.securityGroups?.map { GroupIdentifier().withGroupId(it) })
    .withState(InstanceState().withCode(INSTANCE_RUNNING).withName(InstanceStateName.Running.toString()))
    .withSubnetId(request.subnetId)
    .withTags(request.tagSpecifications.flatMap(TagSpecification::getTags))
    .apply {
        setSecurityGroups((securityGroups + networkInterfaces.flatMap { it.groups }).distinct())
        subnetId = networkInterfaces.firstOrNull { it.subnetId != null }?.subnetId
    }

private fun mapInstanceNetworkInterface(
    interfaceSpecification: InstanceNetworkInterfaceSpecification,
    request: RunInstancesRequest
): InstanceNetworkInterface = InstanceNetworkInterface()
    .withDescription(interfaceSpecification.description)
    .withAssociation(interfaceSpecification.associatePublicIpAddress.mapTrue {
        InstanceNetworkInterfaceAssociation().withIpOwnerId(request.accountId)
            .withPublicDnsName("ec2-1-2-3-4.${request.region}.compute..amazonaws.com")
            .withPublicIp("1.2.3.4")
    })
    .withAttachment(
        InstanceNetworkInterfaceAttachment().withAttachmentId(UUID.randomUUID().toString())
            .withAttachTime(Date())
            .withDeleteOnTermination(interfaceSpecification.deleteOnTermination)
            .withDeviceIndex(interfaceSpecification.deviceIndex)
            .withStatus(AttachmentStatus.Attached)
    )
    .withGroups(interfaceSpecification.groups.map { id -> GroupIdentifier().withGroupId(id) })
    .withInterfaceType(interfaceSpecification.interfaceType)
    .withIpv6Addresses(interfaceSpecification.ipv6Addresses.map { ip ->
        InstanceIpv6Address().withIpv6Address(ip.ipv6Address)
    })
    .withPrivateIpAddress(interfaceSpecification.privateIpAddress)
    .withPrivateIpAddresses(interfaceSpecification.privateIpAddresses.map { ip ->
        InstancePrivateIpAddress().withPrimary(ip.primary)
            .withPrivateIpAddress(ip.privateIpAddress)
    })
    .withStatus(NetworkInterfaceStatus.Associated)
    .withSubnetId(interfaceSpecification.subnetId)

private fun mapInstanceBlockDeviceMapping(it: BlockDeviceMapping): InstanceBlockDeviceMapping =
    InstanceBlockDeviceMapping().withDeviceName(it.deviceName)
        .withEbs(EbsInstanceBlockDevice().withAttachTime(Date())
            .withDeleteOnTermination(it.ebs.deleteOnTermination)
            .withStatus(AttachmentStatus.Attached)
            .withVolumeId(UUID.randomUUID().toString())
        )
