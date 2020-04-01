package com.equisoft.awsmocks.ecs.infrastructure.persistence

import com.amazonaws.services.ecs.model.TaskDefinition
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class TaskDefinitionRepository : ConcurrentMap<String, TaskDefinition> by ConcurrentHashMap() {
    @Synchronized
    fun findByArn(arn: String): TaskDefinition? = values.find { it.taskDefinitionArn == arn }
}
