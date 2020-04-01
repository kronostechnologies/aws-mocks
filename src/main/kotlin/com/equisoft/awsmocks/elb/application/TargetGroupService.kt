package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyTargetGroupRequest
import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroup
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.TargetGroupRepository

class TargetGroupService(
    private val loadBalancerService: LoadBalancerService,
    targetGroupRepository: TargetGroupRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseElbService<TargetGroup, TargetGroupRepository>(
    { it.targetGroupName }, targetGroupRepository, tagsRepository) {
    override val duplicateMessage: String = "DuplicateTargetGroupName"
    override val notFoundMessage: String = "TargetGroupNotFound"

    override fun getArn(value: TargetGroup): String = value.targetGroupArn

    fun modify(arn: String, request: ModifyTargetGroupRequest) {
        getByArn(arn).apply {
            request.healthCheckEnabled?.also(this::setHealthCheckEnabled)
            request.healthCheckIntervalSeconds?.also(this::setHealthCheckIntervalSeconds)
            request.healthCheckPath?.also(this::setHealthCheckPath)
            request.healthCheckPort?.also(this::setHealthCheckPort)
            request.healthCheckProtocol?.also(this::setHealthCheckProtocol)
            request.healthCheckTimeoutSeconds?.also(this::setHealthCheckTimeoutSeconds)
            request.healthyThresholdCount?.also(this::setHealthyThresholdCount)
            request.matcher?.also(this::setMatcher)
            request.unhealthyThresholdCount?.also(this::setUnhealthyThresholdCount)
        }
    }

    fun getAllByLoadBalancerArn(loadBalancerArn: String): List<TargetGroup> {
        val loadBalancer: LoadBalancer = loadBalancerService.getByArn(loadBalancerArn)

        return repository.values.filter { it.loadBalancerArns.contains(loadBalancer.loadBalancerArn) }
    }

    fun getAllByArn(targetGroupArns: List<String>): List<TargetGroup> = targetGroupArns.map(::getByArn)
}
