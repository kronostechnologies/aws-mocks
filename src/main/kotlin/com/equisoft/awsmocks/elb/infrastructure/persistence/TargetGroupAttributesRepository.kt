package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroup
import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroupAttribute

class TargetGroupAttributesRepository : BaseElbRepository<String, List<TargetGroupAttribute>>() {
    @Synchronized
    fun update(targetGroup: TargetGroup, attributes: List<TargetGroupAttribute>) {
        val key = targetGroup.targetGroupArn
        val newAttributes: MutableList<TargetGroupAttribute> = attributes.toMutableList()
        val existingAttributes: List<TargetGroupAttribute> = getOrPut(key) { listOf() }

        existingAttributes.forEach { existingAttribute ->
            newAttributes.find { it.key == existingAttribute.key }
                ?.apply { existingAttribute.value = value }
                ?.also { newAttributes.remove(it) }
        }

        put(key, existingAttributes + newAttributes)
    }
}
