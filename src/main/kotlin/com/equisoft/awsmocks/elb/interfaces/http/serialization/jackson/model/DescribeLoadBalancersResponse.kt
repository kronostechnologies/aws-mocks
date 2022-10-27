package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeLoadBalancersResponse")
class DescribeLoadBalancersResponse(
    val describeLoadBalancersResult: DescribeLoadBalancersResult
) : AmazonWebServiceResult<ResponseMetadata>()
