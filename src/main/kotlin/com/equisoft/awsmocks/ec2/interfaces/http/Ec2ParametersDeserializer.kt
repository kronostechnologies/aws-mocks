package com.equisoft.awsmocks.ec2.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.*
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.common.interfaces.http.toList
import com.equisoft.awsmocks.common.interfaces.http.toMap
import com.equisoft.awsmocks.common.interfaces.http.toObjects
import com.equisoft.awsmocks.common.interfaces.http.toPairs
import io.ktor.http.Parameters

class Ec2ParametersDeserializer : ParametersDeserializer {
    @Suppress("UNCHECKED_CAST", "LongMethod")
    override fun addParameters(parameters: Parameters, request: AmazonWebServiceRequest): AmazonWebServiceRequest =
        when (request) {
            is AssociateRouteTableRequest -> {
                request.withGatewayId(parameters["GatewayId"])
                    .withRouteTableId(parameters["RouteTableId"])
                    .withSubnetId(parameters["SubnetId"])
            }
            is AttachInternetGatewayRequest -> {
                request.withInternetGatewayId(parameters["InternetGatewayId"])
                    .withVpcId(parameters["VpcId"])
            }
            is AuthorizeSecurityGroupEgressRequest -> {
                val ipPermissions: List<IpPermission> = parseIpPermissions(parameters)
                request.withIpPermissions(ipPermissions)
                    .withGroupId(parameters["GroupId"])
            }
            is AuthorizeSecurityGroupIngressRequest -> {
                val ipPermissions: List<IpPermission> = parseIpPermissions(parameters)
                request.withIpPermissions(ipPermissions)
                    .withGroupId(parameters["GroupId"])
            }
            is CreateRouteRequest -> {
                request.withDestinationCidrBlock(parameters["DestinationCidrBlock"])
                    .withDestinationIpv6CidrBlock(parameters["DestinationIpv6CidrBlock"])
                    .withEgressOnlyInternetGatewayId(parameters["EgressOnlyInternetGatewayId"])
                    .withGatewayId(parameters["GatewayId"])
                    .withInstanceId(parameters["InstanceId"])
                    .withLocalGatewayId(parameters["LocalGatewayId"])
                    .withNatGatewayId(parameters["NatGatewayId"])
                    .withNetworkInterfaceId(parameters["NetworkInterfaceId"])
                    .withRouteTableId(parameters["RouteTableId"])
                    .withTransitGatewayId(parameters["TransitGatewayId"])
                    .withTransitGatewayId(parameters["TransitGatewayId"])
                    .withVpcPeeringConnectionId(parameters["VpcPeeringConnectionId"])
            }
            is CreateRouteTableRequest -> {
                request.withVpcId(parameters["VpcId"])
            }
            is CreateSecurityGroupRequest -> {
                request.withGroupName(parameters["GroupName"])
                    .withDescription(parameters["GroupDescription"])
                    .withVpcId(parameters["VpcId"])
            }
            is CreateSubnetRequest -> {
                request.withAvailabilityZone(parameters["AvailabilityZone"])
                    .withAvailabilityZoneId(parameters["AvailabilityZoneId"])
                    .withCidrBlock(parameters["CidrBlock"])
                    .withVpcId(parameters["VpcId"])
            }
            is CreateTagsRequest -> {
                request.withResources(parameters.toList("ResourceId"))
                    .withTags(parameters.getTags())
            }
            is CreateVpcEndpointRequest -> {
                request.withClientToken(parameters["ClientToken"])
                    .withPolicyDocument(parameters["PolicyDocument"])
                    .withPrivateDnsEnabled(parameters["PrivateDnsEnabled"]?.toBoolean())
                    .withServiceName(parameters["ServiceName"])
                    .withTagSpecifications(parseTagsSpecifications(parameters))
                    .withVpcEndpointType(parameters["VpcEndpointType"])
                    .withVpcId(parameters["VpcId"])
            }
            is CreateVpcRequest -> {
                request.withAmazonProvidedIpv6CidrBlock(parameters["AmazonProvidedIpv6CidrBlock"]?.toBoolean())
                    .withCidrBlock(parameters["CidrBlock"])
                    .withInstanceTenancy(parameters["InstanceTenancy"])
            }
            is DescribeAccountAttributesRequest -> {
                request.withAttributeNames(parameters.toList("AttributeName"))
            }
            is DescribeImagesRequest -> {
                request.withFilters(parameters.getFilters())
                    .withOwners(parameters.toList("Owner"))
                    .withImageIds(parameters.toList("ImageId"))
            }
            is DescribeInstanceAttributeRequest -> {
                request.withAttribute(parameters["Attribute"])
                    .withInstanceId(parameters["InstanceId"])
            }
            is DescribeInstanceCreditSpecificationsRequest -> {
                request.withFilters(parameters.getFilters())
                    .withInstanceIds(parameters.toList("InstanceId"))
            }
            is DescribeInstancesRequest -> {
                request.withFilters(parameters.getFilters())
                    .apply {
                        filters.addAll(mapListToFilters(parameters, "InstanceId", "instance-id"))
                    }
            }
            is DescribeInternetGatewaysRequest -> {
                request.withFilters(parameters.getFilters())
                    .withInternetGatewayIds(parameters.toList("InternetGatewayId"))
            }
            is DescribeNetworkAclsRequest -> {
                request.withFilters(parameters.getFilters())
                    .withNetworkAclIds(parameters.toList("NetworkAclId"))
            }
            is DescribePrefixListsRequest -> {
                request.withFilters(parameters.getFilters())
            }
            is DescribeRouteTablesRequest -> {
                request.withFilters(parameters.getFilters())
                    .withRouteTableIds(parameters.toList("RouteTableId"))
            }
            is DescribeSecurityGroupsRequest -> {
                request.withFilters(parameters.getFilters())
                    .withGroupIds(parameters.toList("GroupId"))
                    .withGroupNames(parameters.toList("GroupName"))
            }
            is DescribeSubnetsRequest -> {
                request.withFilters(parameters.getFilters())
                    .withSubnetIds(parameters.toList("SubnetId"))
            }
            is DescribeTagsRequest -> {
                request.withFilters(parameters.getFilters())
            }
            is DescribeVolumesRequest -> {
                request.withFilters(parameters.getFilters())
                    .withVolumeIds(parameters.toList("VolumeId"))
            }
            is DescribeVpcAttributeRequest -> {
                request.withVpcId(parameters["VpcId"])
                    .withAttribute(parameters["Attribute"])
            }
            is DescribeVpcClassicLinkRequest -> {
                request.withFilters(parameters.getFilters())
                    .withVpcIds(parameters.toList("VpcId"))
            }
            is DescribeVpcClassicLinkDnsSupportRequest -> {
                request.withVpcIds(parameters.toList("VpcId"))
            }
            is DescribeVpcEndpointsRequest -> {
                request.withVpcEndpointIds(parameters.toList("VpcEndpointId"))
            }
            is DescribeVpcEndpointServicesRequest -> {
                request.withServiceNames(parameters.toList("ServiceName"))
            }
            is DescribeVpcsRequest -> {
                request.withFilters(parameters.getFilters())
                    .withVpcIds(parameters.toList("VpcId"))
            }
            is DisassociateRouteTableRequest -> {
                request.withAssociationId(parameters["AssociationId"])
            }
            is ModifyInstanceAttributeRequest -> {
                request.withAttribute(parameters["Attribute"])
                    .withEbsOptimized(parameters["EbsOptimized.Value"]?.toBoolean())
                    .withEnaSupport(parameters["EnaSupport.Value"]?.toBoolean())
                    .withGroups(parameters.toList("GroupId"))
                    .withInstanceId(parameters["InstanceId"])
                    .withInstanceInitiatedShutdownBehavior(parameters["InstanceInitiatedShutdownBehavior.Value"])
                    .withInstanceType(parameters["InstanceType.Value"])
                    .withKernel(parameters["Kernel.Value"])
                    .withRamdisk(parameters["Ramdisk.Value"])
                    .withSourceDestCheck(parameters["SourceDestCheck.Value"]?.toBoolean())
                    .withSriovNetSupport(parameters["SriovNetSupport.Value"])
                    .withUserData(parameters["UserData.Value"])
                    .withValue(parameters["Value"])
            }
            is ModifyVpcAttributeRequest -> {
                request.withEnableDnsHostnames(parameters["EnableDnsHostnames"]?.toBoolean())
                    .withEnableDnsSupport(parameters["EnableDnsSupport"]?.toBoolean())
                    .withVpcId(parameters["VpcId"])
            }
            is ModifyVpcEndpointRequest -> {
                request.withVpcEndpointId(parameters["VpcEndpointId"])
                    .withPolicyDocument(parameters["PolicyDocument"])
                    .withResetPolicy(parameters["ResetPolicy"]?.toBoolean())
                    .withAddRouteTableIds(parameters.toList("AddRouteTableId"))
                    .withAddSecurityGroupIds(parameters.toList("AddSecurityGroupId"))
                    .withAddSubnetIds(parameters.toList("AddSubnetId"))
                    .withRemoveRouteTableIds(parameters.toList("RemoveRouteTableId"))
                    .withRemoveSecurityGroupIds(parameters.toList("RemoveSecurityGroupId"))
                    .withRemoveSubnetIds(parameters.toList("RemoveSubnetId"))
            }
            is RevokeSecurityGroupEgressRequest -> {
                val ipPermissions: List<IpPermission> = parseIpPermissions(parameters)
                request.withIpPermissions(ipPermissions)
                    .withGroupId(parameters["GroupId"])
            }
            is RunInstancesRequest -> {
                request.withAdditionalInfo(parameters["AdditionalInfo"])
                    .withClientToken(parameters["ClientToken"])
                    .withCreditSpecification(CreditSpecificationRequest()
                        .withCpuCredits(parameters["CreditSpecification.CpuCredits"]))
                    .withDisableApiTermination(parameters["DisableApiTermination"]?.toBoolean())
                    .withEbsOptimized(parameters["EbsOptimized"]?.toBoolean())
                    .withHibernationOptions(HibernationOptionsRequest()
                        .withConfigured(parameters["HibernationOptions.Configured"]?.toBoolean()))
                    .withIamInstanceProfile(parameters["IamInstanceProfile.Name"]?.let { name ->
                        IamInstanceProfileSpecification()
                            .withArn(parameters["IamInstanceProfile.Arn"])
                            .withName(name)
                    })
                    .withImageId(parameters["ImageId"])
                    .withInstanceInitiatedShutdownBehavior(
                        parameters["InstanceInitiatedShutdownBehavior"] ?: ShutdownBehavior.Stop.toString()
                    )
                    .withInstanceType(parameters["InstanceType"] ?: "m1.small")
                    .withLaunchTemplate(LaunchTemplateSpecification()
                        .withLaunchTemplateId(parameters["LaunchTemplate.LaunchTemplateId"])
                        .withLaunchTemplateName(parameters["LaunchTemplate.LaunchTemplateName"])
                        .withVersion(parameters["LaunchTemplate.Version"]))
                    .withMaxCount(parameters["MaxCount"]?.toInt())
                    .withMinCount(parameters["MinCount"]?.toInt())
                    .withMonitoring(parameters["Monitoring.Enabled"]?.toBoolean())
                    .withNetworkInterfaces(parseNetworkInterfaces(parameters))
                    .withPlacement(Placement()
                        .withAffinity(parameters["Placement.Affinity"])
                        .withAvailabilityZone(parameters["Placement.AvailabilityZone"])
                        .withGroupName(parameters["Placement.GroupName"])
                        .withHostId(parameters["Placement.HostId"])
                        .withHostResourceGroupArn(parameters["Placement.HostResourceGroupArn"])
                        .withPartitionNumber(parameters["Placement.PartitionNumber"]?.toInt())
                        .withSpreadDomain(parameters["Placement.SpreadDomain"])
                        .withTenancy(parameters["Placement.Tenancy"])
                    )
                    .withPrivateIpAddress(parameters["PrivateIpAddress"])
                    .withRamdiskId(parameters["RamdiskId"])
                    .withSecurityGroups(parameters.toList("SecurityGroup"))
                    .withSecurityGroupIds(parameters.toList("SecurityGroupId"))
                    .withSubnetId(parameters["SubnetId"])
                    .withTagSpecifications(parseTagsSpecifications(parameters))
            }
            is TerminateInstancesRequest -> {
                request.withInstanceIds(parameters.toList("InstanceId"))
            }
            else -> request
        }

    private fun mapListToFilters(parameters: Parameters, parameterName: String, filterName: String): List<Filter> =
        parameters.toList(parameterName).map { value ->
            Filter().withName(filterName).withValues(value)
        }

    private fun parseTagsSpecifications(parameters: Parameters): List<TagSpecification> {
        return parameters.toObjects("TagSpecification",
            ::TagSpecification) { index, tagSpecification, (key, value) ->
            when {
                key == "ResourceType" -> tagSpecification.withResourceType(value)
                key.startsWith("Tag") -> tagSpecification.withTags(parameters.getTags("TagSpecification.$index.Tag"))
                else -> tagSpecification
            }
        }
    }

    private fun parseNetworkInterfaces(parameters: Parameters): List<InstanceNetworkInterfaceSpecification> =
        parameters.toObjects("NetworkInterface",
            ::InstanceNetworkInterfaceSpecification) { index, networkInterface, (key, value) ->
            buildNetworkInterface(index, key, value, parameters, networkInterface)
        }

    private fun buildNetworkInterface(
        index: Int,
        key: String,
        value: String,
        parameters: Parameters,
        networkInterface: InstanceNetworkInterfaceSpecification
    ): InstanceNetworkInterfaceSpecification = when {
        key == "AssociatePublicIpAddress" -> networkInterface.withAssociatePublicIpAddress(value.toBoolean())
        key == "DeleteOnTermination" -> networkInterface.withDeleteOnTermination(value.toBoolean())
        key == "Description" -> networkInterface.withDescription(value)
        key == "DeviceIndex" -> networkInterface.withDeviceIndex(value.toInt())
        key == "Ipv6AddressCount" -> networkInterface.withIpv6AddressCount(value.toInt())
        key.startsWith("Ipv6Addresses") -> networkInterface
            .withIpv6Addresses(parseInstanceIpv6Addresses(parameters, index))
        key == "InterfaceType" -> networkInterface.withInterfaceType(value)
        key == "NetworkInterfaceId" -> networkInterface.withNetworkInterfaceId(value)
        key == "PrivateIpAddress" -> networkInterface.withPrivateIpAddress(value)
        key.startsWith("PrivateIpAddresses") ->
            networkInterface.withPrivateIpAddresses(parsePrivateIpAddresses(parameters, index))
        key == "SecondaryPrivateIpAddressCount" ->
            networkInterface.withSecondaryPrivateIpAddressCount(value.toInt())
        key.startsWith("Groups") -> networkInterface.withGroups(parseGroups(parameters, index))
        key.startsWith("SecurityGroupId") -> networkInterface.withGroups(parseSecurityGroups(parameters, index))
        key == "SubnetId" -> networkInterface.withSubnetId(value)
        else -> networkInterface
    }

    private fun parseInstanceIpv6Addresses(parameters: Parameters, index: Int): List<InstanceIpv6Address> {
        return parameters.toObjects("NetworkInterface.$index.Ipv6Addresses",
            ::InstanceIpv6Address) { _, ipAddress, (key, value) ->
            when (key) {
                "Ipv6Address" -> ipAddress.withIpv6Address(value)
                else -> ipAddress
            }
        }
    }

    private fun parseGroups(parameters: Parameters, index: Int): List<String> =
        parameters.toList("NetworkInterface.$index.Groups")

    private fun parseSecurityGroups(parameters: Parameters, index: Int): List<String> =
        parameters.toList("NetworkInterface.$index.SecurityGroupId")

    private fun parsePrivateIpAddresses(parameters: Parameters, index: Int): List<PrivateIpAddressSpecification> =
        parameters.toObjects("NetworkInterface.$index.PrivateIpAddresses",
            ::PrivateIpAddressSpecification) { _, ipAddress, (key, value) ->
            when (key) {
                "Primary" -> ipAddress.withPrimary(value.toBoolean())
                "PrivateIpAddress" -> ipAddress.withPrivateIpAddress(value)
                else -> ipAddress
            }
        }

    private fun parseIpPermissions(parameters: Parameters): List<IpPermission> =
        parameters.toObjects("IpPermissions", ::IpPermission) { index, ipPermission, (key, value) ->
            when {
                key == "FromPort" -> ipPermission.withFromPort(value.toInt())
                key == "IpProtocol" -> ipPermission.withIpProtocol(value)
                key == "ToPort" -> ipPermission.withToPort(value.toInt())
                key.startsWith("IpRanges") -> {
                    val ipRanges: List<IpRange> = parseIpPermissionIpRanges(parameters, index)
                    ipPermission.withIpv4Ranges(ipRanges)
                }
                else -> ipPermission
            }
        }

    private fun parseIpPermissionIpRanges(parameters: Parameters, index: Int): List<IpRange> =
        parameters.toObjects("IpPermissions.$index.IpRanges", ::IpRange) { _, ipRange, (key, value) ->
            when (key) {
                "CidrIp" -> ipRange.withCidrIp(value)
                "Description" -> ipRange.withDescription(value)
                else -> ipRange
            }
        }
}

private fun Parameters.getFilters(): List<Filter> = toMap("Filter", "Name", "Value")
    .map { Filter().withName(it.key).withValues(it.value) }

private fun Parameters.getTags(name: String = "Tag"): List<Tag> = toPairs(name, "Key", "Value")
    .map { (key, value) -> Tag().withKey(key).withValue(value) }
