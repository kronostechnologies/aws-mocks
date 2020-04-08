package com.equisoft.awsmocks.ecs.context

import com.amazonaws.services.ecs.model.Tag
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.JsonRequestFactory
import com.equisoft.awsmocks.ecs.application.CapacityProviderService
import com.equisoft.awsmocks.ecs.application.ClusterService
import com.equisoft.awsmocks.ecs.application.EcsRequestHandler
import com.equisoft.awsmocks.ecs.application.ServiceService
import com.equisoft.awsmocks.ecs.application.TaskDefinitionService
import com.equisoft.awsmocks.ecs.infrastructure.persistence.CapacityProviderRepository
import com.equisoft.awsmocks.ecs.infrastructure.persistence.ClusterRepository
import com.equisoft.awsmocks.ecs.infrastructure.persistence.ServiceRepository
import com.equisoft.awsmocks.ecs.infrastructure.persistence.TaskDefinitionRepository
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.koin.core.module.Module
import org.koin.dsl.module

fun ecsModules(): List<Module> {
    return listOf(module {
        single { CapacityProviderService(get()) }
        single { ClusterService(get(), get()) }
        single { ServiceService(get()) }
        single { TaskDefinitionService(get()) }

        single { CapacityProviderRepository() }
        single { ClusterRepository() }
        single { ServiceRepository() }
        single { TaskDefinitionRepository() }
        single { ResourceTagsRepository<Tag> { value } }

        single { EcsRequestHandler(get(), get(), get(), get(), get()) }

        single { JsonRequestFactory(get(), "com.amazonaws.services.ecs.model") }

        single { xmlMapper() }
        single { objectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE) }
    }, contentConvertersModule())
}
