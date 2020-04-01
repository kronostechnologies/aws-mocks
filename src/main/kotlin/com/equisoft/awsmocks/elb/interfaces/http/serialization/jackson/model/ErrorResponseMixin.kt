package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.exceptions.Error
import com.fasterxml.jackson.annotation.JsonProperty
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "ErrorResponse")
interface ErrorResponseMixin {
    @JsonProperty("Error")
    fun getError(): Error
}
