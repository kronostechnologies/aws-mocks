package com.equisoft.awsmocks.autoscaling.application

import com.amazonaws.services.autoscaling.model.LaunchConfiguration
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.autoscaling.infrastructure.persistence.LaunchConfigurationRepository
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository

class LaunchConfigurationService(
    launchConfigurationRepository: LaunchConfigurationRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseAutoScalingService<LaunchConfiguration, LaunchConfigurationRepository>(
    { it.launchConfigurationName }, launchConfigurationRepository, tagsRepository)
