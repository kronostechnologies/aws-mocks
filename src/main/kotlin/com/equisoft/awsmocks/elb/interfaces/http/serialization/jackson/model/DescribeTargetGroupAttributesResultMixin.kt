package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroupAttribute
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeTargetGroupAttributesResultMixin {
    @ListMember
    fun getAttributes(): List<TargetGroupAttribute>
}
