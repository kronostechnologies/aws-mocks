package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.VpcEndpoint
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeVpcEndpointsResponse")
interface DescribeVpcEndpointsMixin {
    @ListItem("vpcEndpointSet")
    fun getVpcEndpoints(): List<VpcEndpoint>
}
