package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.AvailabilityZone
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface LoadBalancerMixin {
    @ListMember
    fun getAvailabilityZones(): List<AvailabilityZone>

    @ListMember
    fun getSecurityGroups(): List<String>
}
