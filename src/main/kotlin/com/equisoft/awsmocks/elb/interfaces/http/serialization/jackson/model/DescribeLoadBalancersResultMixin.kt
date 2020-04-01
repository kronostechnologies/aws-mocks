package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeLoadBalancersResultMixin {
    @ListMember
    fun getLoadBalancers(): List<LoadBalancer>
}
