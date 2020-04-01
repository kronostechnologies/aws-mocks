package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancerAttribute
import com.equisoft.awsmocks.elb.infrastructure.persistence.LoadBalancerAttributesRepository

class LoadBalancerAttributesService(
    private val loadBalancerService: LoadBalancerService,
    private val attributesRepository: LoadBalancerAttributesRepository
) {
    fun modifyAttributes(targetGroupArn: String, attributes: List<LoadBalancerAttribute>) {
        val targetGroup = loadBalancerService.getByArn(targetGroupArn)

        attributesRepository.update(targetGroup, attributes)
    }

    fun getByArn(loadBalancerArn: String): List<LoadBalancerAttribute> {
        val loadBalancer = loadBalancerService.getByArn(loadBalancerArn)

        return attributesRepository[loadBalancer.loadBalancerArn] ?: listOf()
    }
}
