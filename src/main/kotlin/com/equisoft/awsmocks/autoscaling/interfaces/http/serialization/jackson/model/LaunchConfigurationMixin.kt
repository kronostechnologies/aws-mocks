package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface LaunchConfigurationMixin {
    @ListMember
    fun getClassicLinkVPCSecurityGroups(): List<String>

    @ListMember
    fun getSecurityGroups(): List<String>
}
