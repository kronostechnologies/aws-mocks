package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeListenersResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeListenersResponse")
class DescribeListenersResponse(
    val describeListenersResult: DescribeListenersResult
) : AmazonWebServiceResult<ResponseMetadata>()
