package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.AsValue
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeVpcAttributeResponse")
interface DescribeVpcAttributeResultMixin {
    @AsValue
    fun getEnableDnsHostnames(): Boolean

    @AsValue
    fun getEnableDnsSupport(): Boolean
}
