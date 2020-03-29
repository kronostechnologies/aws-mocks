package com.equisoft.awsmocks.autoscaling.infrastructure.persistence

import com.amazonaws.services.autoscaling.model.LaunchConfiguration

class LaunchConfigurationRepository : BaseAutoScalingRepository<String, LaunchConfiguration>()
