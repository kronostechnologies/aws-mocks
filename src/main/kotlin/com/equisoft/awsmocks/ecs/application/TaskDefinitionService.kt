package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.TaskDefinition
import com.equisoft.awsmocks.common.exceptions.ResourceNotFoundException
import com.equisoft.awsmocks.ecs.infrastructure.persistence.TaskDefinitionRepository

class TaskDefinitionService(
    private val taskDefinitionRepository: TaskDefinitionRepository
) {
    fun add(value: TaskDefinition) {
        val existingTask: TaskDefinition? = taskDefinitionRepository[value.family]
        if (existingTask != null) {
            value.revision = existingTask.revision + 1
        }
        taskDefinitionRepository[value.family] = value
    }

    fun getByArn(arn: String): TaskDefinition = taskDefinitionRepository.findByArn(arn)
        ?: throw ResourceNotFoundException()

    fun deregister(arn: String): TaskDefinition = getByArn(arn).also {
        taskDefinitionRepository.remove(it.family)
    }
}
