package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeTargetGroupsResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeTargetGroupsResponse")
class DescribeTargetGroupsResponse(
    val describeTargetGroupsResult: DescribeTargetGroupsResult
) : AmazonWebServiceResult<ResponseMetadata>()
