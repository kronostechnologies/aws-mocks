package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.ClientVpnEndpoint
import com.amazonaws.services.ec2.model.CustomerGateway
import com.amazonaws.services.ec2.model.DhcpOptions
import com.amazonaws.services.ec2.model.FleetData
import com.amazonaws.services.ec2.model.FlowLog
import com.amazonaws.services.ec2.model.FpgaImage
import com.amazonaws.services.ec2.model.HostReservation
import com.amazonaws.services.ec2.model.Image
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.InternetGateway
import com.amazonaws.services.ec2.model.KeyPair
import com.amazonaws.services.ec2.model.LaunchTemplate
import com.amazonaws.services.ec2.model.NatGateway
import com.amazonaws.services.ec2.model.NetworkAcl
import com.amazonaws.services.ec2.model.NetworkInterface
import com.amazonaws.services.ec2.model.PlacementGroup
import com.amazonaws.services.ec2.model.ReservedInstances
import com.amazonaws.services.ec2.model.ResourceType
import com.amazonaws.services.ec2.model.RouteTable
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.ec2.model.Snapshot
import com.amazonaws.services.ec2.model.SpotFleetRequestConfig
import com.amazonaws.services.ec2.model.SpotInstanceRequest
import com.amazonaws.services.ec2.model.Subnet
import com.amazonaws.services.ec2.model.TrafficMirrorFilter
import com.amazonaws.services.ec2.model.TrafficMirrorSession
import com.amazonaws.services.ec2.model.TrafficMirrorTarget
import com.amazonaws.services.ec2.model.TransitGateway
import com.amazonaws.services.ec2.model.TransitGatewayAttachment
import com.amazonaws.services.ec2.model.TransitGatewayMulticastDomain
import com.amazonaws.services.ec2.model.TransitGatewayRouteTable
import com.amazonaws.services.ec2.model.Volume
import com.amazonaws.services.ec2.model.Vpc
import com.amazonaws.services.ec2.model.VpcPeeringConnection
import com.amazonaws.services.ec2.model.VpnConnection
import com.amazonaws.services.ec2.model.VpnGateway

fun findResourceType(resource: Any?): ResourceType? = resource?.let(::mapClassToResourceType)

private fun mapClassToResourceType(it: Any): ResourceType = when (it) {
    is ClientVpnEndpoint -> ResourceType.ClientVpnEndpoint
    is CustomerGateway -> ResourceType.CustomerGateway
    is DhcpOptions -> ResourceType.DhcpOptions
    is FleetData -> ResourceType.Fleet
    is FpgaImage -> ResourceType.FpgaImage
    is HostReservation -> ResourceType.HostReservation
    is Image -> ResourceType.Image
    is Instance -> ResourceType.Instance
    is InternetGateway -> ResourceType.InternetGateway
    is KeyPair -> ResourceType.KeyPair
    is LaunchTemplate -> ResourceType.LaunchTemplate
    is NatGateway -> ResourceType.Natgateway
    is NetworkAcl -> ResourceType.NetworkAcl
    is NetworkInterface -> ResourceType.NetworkInterface
    is PlacementGroup -> ResourceType.PlacementGroup
    is ReservedInstances -> ResourceType.ReservedInstances
    is RouteTable -> ResourceType.RouteTable
    is SecurityGroup -> ResourceType.SecurityGroup
    is Snapshot -> ResourceType.Snapshot
    is SpotFleetRequestConfig -> ResourceType.SpotFleetRequest
    is SpotInstanceRequest -> ResourceType.SpotInstancesRequest
    is Subnet -> ResourceType.Subnet
    is TrafficMirrorFilter -> ResourceType.TrafficMirrorFilter
    is TrafficMirrorSession -> ResourceType.TrafficMirrorSession
    is TrafficMirrorTarget -> ResourceType.TrafficMirrorTarget
    is TransitGateway -> ResourceType.TransitGateway
    is TransitGatewayAttachment -> ResourceType.TransitGatewayAttachment
    is TransitGatewayMulticastDomain -> ResourceType.TransitGatewayMulticastDomain
    is TransitGatewayRouteTable -> ResourceType.TransitGatewayRouteTable
    is Volume -> ResourceType.Volume
    is Vpc -> ResourceType.Vpc
    is VpcPeeringConnection -> ResourceType.VpcPeeringConnection
    is VpnConnection -> ResourceType.VpnConnection
    is VpnGateway -> ResourceType.VpnGateway
    is FlowLog -> ResourceType.VpcFlowLog
    else -> ResourceType.valueOf(it::class.simpleName.toString())
}
