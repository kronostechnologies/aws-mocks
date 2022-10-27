package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.ServiceDetail
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeVpcEndpointServicesResponse")
interface DescribeVpcEndpointServicesMixin {
    @ListItem("serviceNameSet")
    fun getServiceNames(): List<String>

    @ListItem("serviceDetailSet")
    fun getServiceDetails(): List<ServiceDetail>
}
