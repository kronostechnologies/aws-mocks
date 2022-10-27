package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.SetSecurityGroupsResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "SetSecurityGroupsResponse")
class SetSecurityGroupsResponse(
    val setSecurityGroupsResult: SetSecurityGroupsResult
) : AmazonWebServiceResult<ResponseMetadata>()
