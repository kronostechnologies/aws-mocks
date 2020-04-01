package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeTargetGroupAttributesResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeTargetGroupAttributesResponse")
class DescribeTargetGroupAttributesResponse(
    val describeTargetGroupAttributesResult: DescribeTargetGroupAttributesResult
) : AmazonWebServiceResult<ResponseMetadata>()
