package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.CreateLoadBalancerResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "CreateLoadBalancerResponse")
class CreateLoadBalancerResponse(
    val createLoadBalancerResult: CreateLoadBalancerResult
) : AmazonWebServiceResult<ResponseMetadata>()
