package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeAutoScalingGroupsResponse")
class DescribeAutoScalingGroupsResponse(
    val describeAutoScalingGroupsResult: DescribeAutoScalingGroupsResult
) : AmazonWebServiceResult<ResponseMetadata>()
