package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.services.autoscaling.model.NotificationConfiguration
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface DescribeNotificationConfigurationsResultMixin {
    @ListMember
    fun getNotificationConfigurations(): List<NotificationConfiguration>
}
