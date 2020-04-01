package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.services.autoscaling.model.LaunchConfiguration
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeLaunchConfigurationsResultMixin {
    @ListMember
    fun getLaunchConfigurations(): List<LaunchConfiguration>
}
