package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.*

fun findResourceType(resource: Any?): ResourceType? = resource?.let(::mapClassToResourceType)

private fun mapClassToResourceType(it: Any): ResourceType? = when (it) {
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
    else -> null
}
