package com.equisoft.awsmocks.autoscaling.infrastructure.persistence

import com.amazonaws.services.autoscaling.model.AutoScalingGroup

class AutoScalingGroupRepository : BaseAutoScalingRepository<String, AutoScalingGroup>()
