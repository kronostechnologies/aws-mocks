package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancerAttributesResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeLoadBalancerAttributesResponse")
class DescribeLoadBalancerAttributesResponse(
    val describeLoadBalancerAttributesResult: DescribeLoadBalancerAttributesResult
) : AmazonWebServiceResult<ResponseMetadata>()
