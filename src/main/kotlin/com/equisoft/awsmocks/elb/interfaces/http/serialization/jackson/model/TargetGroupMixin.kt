package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface TargetGroupMixin {
    @ListMember
    fun getLoadBalancerArns(): List<String>
}
