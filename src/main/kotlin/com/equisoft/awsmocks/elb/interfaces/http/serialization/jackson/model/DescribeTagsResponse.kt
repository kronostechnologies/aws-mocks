package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeTagsResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeTagsResponse")
class DescribeTagsResponse(
    val describeTagsResult: DescribeTagsResult
) : AmazonWebServiceResult<ResponseMetadata>()
