package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.CreateRuleResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "CreateRuleResponse")
class CreateRuleResponse(
    val createRuleResult: CreateRuleResult
) : AmazonWebServiceResult<ResponseMetadata>()
