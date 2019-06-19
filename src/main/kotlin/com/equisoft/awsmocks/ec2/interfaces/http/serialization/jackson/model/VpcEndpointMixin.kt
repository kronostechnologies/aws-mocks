package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.DnsEntry
import com.amazonaws.services.ec2.model.SecurityGroupIdentifier
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface VpcEndpointMixin {
    @ListItem("routeTableIdSet")
    fun getRouteTableIds(): List<String>

    @ListItem("subnetIdSet")
    fun getSubnetIds(): List<String>

    @ListItem("groupSet")
    fun getGroups(): List<SecurityGroupIdentifier>

    @ListItem("networkInterfaceIdSet")
    fun getNetworkInterfaceIds(): List<String>

    @ListItem("dnsEntrySet")
    fun getDnsEntries(): List<DnsEntry>
}
