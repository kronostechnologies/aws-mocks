package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface TagDescriptionMixin {
    @ListMember
    fun getTags(): List<Tag>
}
