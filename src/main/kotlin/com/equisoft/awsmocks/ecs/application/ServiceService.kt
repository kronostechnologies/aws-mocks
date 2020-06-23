package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.Failure
import com.amazonaws.services.ecs.model.Service
import com.amazonaws.services.ecs.model.UpdateServiceRequest
import com.equisoft.awsmocks.ecs.infrastructure.persistence.ServiceRepository

class ServiceService(
    private val serviceRepository: ServiceRepository
) {
    fun add(value: Service) {
        serviceRepository[value.serviceArn] = value
    }

    fun findByArn(arn: String): Service? = serviceRepository.findByArn(arn)

    fun getAll(clusterArn: String, serviceArns: List<String>): SearchResult<Service> {
        val found: List<Service> = serviceRepository.findByClusterArn(clusterArn, serviceArns)

        return SearchResult<Service>()
            .withFound(found)
            .withFailures(serviceArns.filter { serviceArn ->
                found.none { it.serviceArn == serviceArn }
            }.map { Failure().withArn(it) })
    }

    fun update(arn: String, request: UpdateServiceRequest): Service? = findByArn(arn)
        ?.apply {
            request.desiredCount?.also { withDesiredCount(it).withRunningCount(it) }
        }

    fun delete(arn: String): Service? = findByArn(arn)
        ?.apply { withStatus("INACTIVE") }
//        ?.also { serviceRepository.remove(arn) }
}
