package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember
import com.fasterxml.jackson.annotation.JsonUnwrapped

interface SourceIpConditionConfigMixin {
    @ListMember
    @JsonUnwrapped
    fun getValues(): List<String>
}
