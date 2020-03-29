package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.services.autoscaling.model.Instance
import com.amazonaws.services.autoscaling.model.Tag
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface AutoScalingGroupMixin {
    @ListMember
    fun getLoadBalancerNames(): List<String>

    @ListMember
    fun getInstances(): List<Instance>

    @ListMember
    fun getTerminationPolicies(): List<String>

    @ListMember
    fun getAvailabilityZones(): List<String>

    @ListMember
    fun getTargetGroupARNs(): List<String>

    @ListMember
    fun getTags(): List<Tag>
}
