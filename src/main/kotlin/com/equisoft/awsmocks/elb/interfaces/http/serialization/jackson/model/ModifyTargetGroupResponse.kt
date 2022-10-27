package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyTargetGroupResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "ModifyTargetGroupResponse")
class ModifyTargetGroupResponse(
    val modifyTargetGroupResult: ModifyTargetGroupResult
) : AmazonWebServiceResult<ResponseMetadata>()
