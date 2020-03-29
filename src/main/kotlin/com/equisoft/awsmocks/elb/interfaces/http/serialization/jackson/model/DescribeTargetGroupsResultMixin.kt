package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroup
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeTargetGroupsResultMixin {
    @ListMember
    fun getTargetGroups(): List<TargetGroup>

    @ListMember
    fun getLoadBalancerArns(): List<String>
}
