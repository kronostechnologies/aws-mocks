package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "ModifyVpcEndpointResponse")
interface ModifyVpcEndpointMixin {
    @JsonIgnore
    fun getReturnValue(): Boolean
}
