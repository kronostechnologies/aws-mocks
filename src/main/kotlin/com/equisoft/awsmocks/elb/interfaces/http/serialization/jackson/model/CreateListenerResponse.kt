package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.CreateListenerResult
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "CreateListenerResponse")
class CreateListenerResponse(
    val createListenerResult: CreateListenerResult
) : AmazonWebServiceResult<ResponseMetadata>()
