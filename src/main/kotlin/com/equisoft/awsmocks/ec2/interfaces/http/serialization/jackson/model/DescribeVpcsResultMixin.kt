package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.Vpc
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeVpcsResponse")
interface DescribeVpcsResultMixin {
    @ListItem("vpcSet")
    fun getVpcs(): List<Vpc>
}
