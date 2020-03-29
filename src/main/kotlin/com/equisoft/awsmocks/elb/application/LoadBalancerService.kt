package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer
import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.LoadBalancerRepository

class LoadBalancerService(
    loadBalancerRepository: LoadBalancerRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseElbService<LoadBalancer, LoadBalancerRepository>(
    { it.loadBalancerName }, loadBalancerRepository, tagsRepository) {
    override val duplicateMessage: String = "DuplicateLoadBalancerName"
    override val notFoundMessage: String = "LoadBalancerNotFound"

    override fun getArn(value: LoadBalancer): String = value.loadBalancerArn

    fun getAllByArn(loadBalancerArns: List<String>): List<LoadBalancer> = loadBalancerArns.map(::getByArn)

    fun setSecurityGroups(loadBalancerArn: String, securityGroups: List<String>) {
        val loadBalancer = getByArn(loadBalancerArn).withSecurityGroups(securityGroups)

        repository[loadBalancer.loadBalancerName] = loadBalancer
    }
}
