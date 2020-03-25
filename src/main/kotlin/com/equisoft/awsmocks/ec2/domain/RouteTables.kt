package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Route
import com.amazonaws.services.ec2.model.RouteTable

fun List<RouteTable>.applyFilters(filters: List<Filter>): List<RouteTable> = filter { routeTable ->
    filters.any { filter ->
        val routes: MutableList<Route> = routeTable.routes
        val testValue: String = extractValueForFilter(filter, routeTable, routes)
        filter.matches(testValue)
    }
}.distinct()

private fun extractValueForFilter(filter: Filter, routeTable: RouteTable, routes: MutableList<Route>): String =
    when (filter.name) {
        "owner-id" -> routeTable.ownerId
        "route-table-id" -> routeTable.routeTableId
        "route.destination-cidr-block " -> routes.joinToString { it.destinationCidrBlock }
        "route.destination-ipv6-cidr-block" -> routes.joinToString { it.destinationIpv6CidrBlock }
        "route.destination-prefix-list-id" -> routes.joinToString { it.destinationPrefixListId }
        "route.egress-only-internet-gateway-id" -> routes.joinToString { it.egressOnlyInternetGatewayId }
        "route.gateway-id " -> routes.joinToString { it.gatewayId }
        "route.instance-id" -> routes.joinToString { it.instanceId }
        "route.nat-gateway-id" -> routes.joinToString { it.natGatewayId }
        "route.transit-gateway-id" -> routes.joinToString { it.transitGatewayId }
        "route.origin" -> routes.joinToString { it.origin }
        "route.state" -> routes.joinToString { it.state }
        "route.vpc-peering-connection-id" -> routes.joinToString { it.vpcPeeringConnectionId }
        "transit-gateway-id" -> routes.joinToString { it.transitGatewayId }
        "vpc-id" -> routeTable.vpcId
        else -> ""
    }
