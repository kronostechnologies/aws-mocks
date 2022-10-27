package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeRulesResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeRulesResponse")
class DescribeRulesResponse(
    val describeRulesResult: DescribeRulesResult
) : AmazonWebServiceResult<ResponseMetadata>()
