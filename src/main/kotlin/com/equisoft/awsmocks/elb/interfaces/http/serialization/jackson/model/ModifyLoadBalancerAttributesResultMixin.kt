package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancerAttribute
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface ModifyLoadBalancerAttributesResultMixin {
    @ListMember
    fun getAttributes(): List<LoadBalancerAttribute>
}
