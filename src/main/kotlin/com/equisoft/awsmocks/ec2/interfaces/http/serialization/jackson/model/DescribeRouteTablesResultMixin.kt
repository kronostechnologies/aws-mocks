package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.RouteTable
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeRouteTablesResponse")
interface DescribeRouteTablesResultMixin {
    @ListItem("routeTableSet")
    fun getRouteTables(): List<RouteTable>
}
