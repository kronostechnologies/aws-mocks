package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.CreateTargetGroupResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "CreateTargetGroupResponse")
class CreateTargetGroupResponse(
    val createTargetGroupResult: CreateTargetGroupResult
) : AmazonWebServiceResult<ResponseMetadata>()
