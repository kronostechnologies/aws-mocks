package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.InternetGateway
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeInternetGatewaysResponse")
interface DescribeInternetGatewaysResultMixin {
    @ListItem("internetGatewaySet")
    fun getInternetGateways(): List<InternetGateway>
}
