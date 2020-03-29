package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyTargetGroupAttributesResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "ModifyTargetGroupAttributesResponse")
class ModifyTargetGroupAttributesResponse(
    val modifyTargetGroupAttributesResult: ModifyTargetGroupAttributesResult
) : AmazonWebServiceResult<ResponseMetadata>()
