package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.InstanceCreditSpecification
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeInstanceCreditSpecificationsResponse")
interface DescribeInstanceCreditSpecificationsResultMixin {
    @ListItem("instanceCreditSpecificationSet")
    fun getInstanceCreditSpecifications(): List<InstanceCreditSpecification>
}
