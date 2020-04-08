package com.equisoft.awsmocks.autoscaling.infrastructure.persistence

import com.amazonaws.services.autoscaling.model.NotificationConfiguration

class NotificationConfigurationRepository : BaseAutoScalingRepository<String, List<NotificationConfiguration>>() {
    @Synchronized
    fun addAll(notificationConfigurations: List<NotificationConfiguration>) {
        notificationConfigurations.forEach { notificationConfiguration ->
            val savedConfigurations: List<NotificationConfiguration> =
                getOrDefault(notificationConfiguration.autoScalingGroupName, listOf())

            put(notificationConfiguration.autoScalingGroupName, savedConfigurations + notificationConfiguration)
        }
    }

    @Synchronized
    fun findAll(groupNames: List<String>): List<NotificationConfiguration> = groupNames.mapNotNull(this::get).flatten()
}
