package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.AsValue
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeVpcAttributeResponse")
interface DescribeVpcAttributeResultMixin {
    @AsValue
    fun getEnableDnsHostnames(): Boolean

    @AsValue
    fun getEnableDnsSupport(): Boolean
}
