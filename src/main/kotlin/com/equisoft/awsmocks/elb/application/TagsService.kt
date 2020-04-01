package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.amazonaws.services.elasticloadbalancingv2.model.TagDescription
import com.equisoft.awsmocks.common.exceptions.BadRequestException

class TagsService(
    private val loadBalancerService: LoadBalancerService,
    private val targetGroupService: TargetGroupService
) {
    fun addTags(resourceArns: List<String>, tags: List<Tag>) {
        resourceArns.forEach {
            val service = getServiceForResourceArn(it)
            service.addTagsByArn(it, tags)
        }
    }

    private fun isLoadBalancer(arn: String): Boolean = arn.contains("loadbalancer/")

    private fun isTargetGroup(arn: String): Boolean = arn.contains("targetgroup/")

    fun getTags(resourceArns: List<String>): List<TagDescription> = resourceArns.flatMap { resourceArn ->
        getServiceForResourceArn(resourceArn)
            .getTagsByArn(resourceArn)
            .map { tags -> TagDescription().withResourceArn(resourceArn).withTags(tags) }
    }

    private fun getServiceForResourceArn(arn: String): BaseElbService<*, *> {
        if (isLoadBalancer(arn)) {
            return loadBalancerService
        } else if (isTargetGroup(arn)) {
            return targetGroupService
        } else {
            throw BadRequestException("InvalidParameterValue")
        }
    }
}
