package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroupAttribute
import com.equisoft.awsmocks.elb.infrastructure.persistence.TargetGroupAttributesRepository

class TargetGroupAttributesService(
    private val targetGroupService: TargetGroupService,
    private val attributesRepository: TargetGroupAttributesRepository
) {
    fun modifyAttributes(targetGroupArn: String, attributes: List<TargetGroupAttribute>) {
        val targetGroup = targetGroupService.getByArn(targetGroupArn)

        attributesRepository.update(targetGroup, attributes)
    }

    fun getByArn(targetGroupArn: String): List<TargetGroupAttribute> {
        val targetGroup = targetGroupService.getByArn(targetGroupArn)

        return attributesRepository[targetGroup.targetGroupArn] ?: listOf()
    }
}
