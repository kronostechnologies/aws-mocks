package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyLoadBalancerAttributesResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "ModifyTargetGroupAttributesResponse")
class ModifyLoadBalancerAttributesResponse(
    val modifyLoadBalancerAttributesResult: ModifyLoadBalancerAttributesResult
) : AmazonWebServiceResult<ResponseMetadata>()
