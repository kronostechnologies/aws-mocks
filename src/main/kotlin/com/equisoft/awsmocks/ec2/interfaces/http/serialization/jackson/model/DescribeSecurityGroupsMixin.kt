package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.SecurityGroup
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeSecurityGroupsResponse")
interface DescribeSecurityGroupsMixin {
    @ListItem("securityGroupInfo")
    fun getSecurityGroups(): List<SecurityGroup>
}
