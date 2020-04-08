package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.CapacityProvider
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.ecs.infrastructure.persistence.CapacityProviderRepository

class CapacityProviderService(
    private val capacityProviderRepository: CapacityProviderRepository
) {
    fun add(value: CapacityProvider) {
        capacityProviderRepository[value.name] = value
    }

    fun getByArn(arn: String): CapacityProvider = capacityProviderRepository.findByArn(arn) ?: throw NotFoundException()

    fun getAllByArn(arns: List<String>): List<CapacityProvider> = arns.map(::getByArn)
}
