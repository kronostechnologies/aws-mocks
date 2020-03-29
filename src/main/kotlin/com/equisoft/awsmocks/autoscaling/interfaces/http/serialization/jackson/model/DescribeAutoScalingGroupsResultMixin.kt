package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeAutoScalingGroupsResultMixin {
    @ListMember
    fun getAutoScalingGroups(): List<AutoScalingGroup>
}
