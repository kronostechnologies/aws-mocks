package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.TagDescription
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeTagsResultMixin {
    @ListMember
    fun getTagDescriptions(): List<TagDescription>
}
