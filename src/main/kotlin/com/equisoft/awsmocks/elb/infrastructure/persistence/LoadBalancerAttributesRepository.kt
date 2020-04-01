package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer
import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancerAttribute

class LoadBalancerAttributesRepository : BaseElbRepository<String, List<LoadBalancerAttribute>>() {
    @Synchronized
    fun update(loadBalancer: LoadBalancer, attributes: List<LoadBalancerAttribute>) {
        val key = loadBalancer.loadBalancerArn
        val newAttributes: MutableList<LoadBalancerAttribute> = attributes.toMutableList()
        val existingAttributes: List<LoadBalancerAttribute> = getOrPut(key) { listOf() }

        existingAttributes.forEach { existingAttribute ->
            newAttributes.find { it.key == existingAttribute.key }
                ?.apply { existingAttribute.value = value }
                ?.also { newAttributes.remove(it) }
        }

        put(key, existingAttributes + newAttributes)
    }
}
