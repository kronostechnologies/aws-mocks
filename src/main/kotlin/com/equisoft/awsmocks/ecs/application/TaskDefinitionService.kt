package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.TaskDefinition
import com.equisoft.awsmocks.common.exceptions.ResourceNotFoundException
import com.equisoft.awsmocks.ecs.infrastructure.persistence.TaskDefinitionRepository

class TaskDefinitionService(
    private val taskDefinitionRepository: TaskDefinitionRepository
) {
    fun add(value: TaskDefinition) {
        taskDefinitionRepository[value.family] = value
    }

    fun getByArn(arn: String): TaskDefinition = taskDefinitionRepository.findByArn(arn)
        ?: throw ResourceNotFoundException()
}
