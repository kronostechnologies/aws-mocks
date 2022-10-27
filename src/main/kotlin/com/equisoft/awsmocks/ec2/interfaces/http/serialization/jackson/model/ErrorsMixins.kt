package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.exceptions.Error
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.WrapError
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Response")
interface ErrorResponseMixin {
    @WrapError("Errors")
    fun getError(): Error
}

@XmlRootElement(name = "Error")
interface ErrorMixin {
    @JsonProperty("Code")
    fun getCode(): String

    @JsonProperty("Message")
    fun getMessage(): String
}
