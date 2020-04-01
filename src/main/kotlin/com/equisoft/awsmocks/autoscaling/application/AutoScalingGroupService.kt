package com.equisoft.awsmocks.autoscaling.application

import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.autoscaling.infrastructure.persistence.AutoScalingGroupRepository
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository

class AutoScalingGroupService(
    autoScalingGroupRepository: AutoScalingGroupRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseAutoScalingService<AutoScalingGroup, AutoScalingGroupRepository>(
    { it.autoScalingGroupName }, autoScalingGroupRepository, tagsRepository) {

    fun attachTargetGroup(name: String, targetGroupArns: List<String>) {
        find(name)?.apply { targetGroupARNs.addAll(targetGroupArns) }?.also(::update)
    }
}
