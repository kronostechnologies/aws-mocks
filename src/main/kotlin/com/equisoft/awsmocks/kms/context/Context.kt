package com.equisoft.awsmocks.kms.context

import com.amazonaws.services.kms.model.Tag
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.JsonRequestFactory
import com.equisoft.awsmocks.kms.application.KeyService
import com.equisoft.awsmocks.kms.application.KmsRequestHandler
import com.equisoft.awsmocks.kms.infrastructure.persistence.KeyAliasRepository
import com.equisoft.awsmocks.kms.infrastructure.persistence.KeyMetadataRepository
import org.koin.core.module.Module
import org.koin.dsl.module

fun kmsModules(): List<Module> {
    return listOf(module {
        single { KeyMetadataRepository() }
        single { KeyAliasRepository() }
        single { ResourceTagsRepository<Tag> { tagValue } }
        single { KeyService(get(), get(), get()) }
        single { KmsRequestHandler(get()) }
        single { JsonRequestFactory(get(), "com.amazonaws.services.kms.model") }

        single { xmlMapper() }
        single { objectMapper() }
    }, contentConvertersModule())
}
