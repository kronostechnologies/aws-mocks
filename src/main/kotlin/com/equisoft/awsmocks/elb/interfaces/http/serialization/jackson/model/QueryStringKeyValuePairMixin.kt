package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.fasterxml.jackson.annotation.JsonUnwrapped

interface QueryStringKeyValuePairMixin {
    @JsonUnwrapped
    fun getValue(): String
}
