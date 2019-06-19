package com.equisoft.awsmocks.acm.context

import com.amazonaws.services.certificatemanager.model.Tag
import com.equisoft.awsmocks.acm.application.AcmRequestHandler
import com.equisoft.awsmocks.acm.application.CertificateService
import com.equisoft.awsmocks.acm.infrastructure.persistence.CertificateRepository
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.JsonRequestFactory
import org.koin.core.module.Module
import org.koin.dsl.module

fun acmModules(): List<Module> {
    return listOf(module {
        single { CertificateService(get(), get()) }

        single { CertificateRepository() }
        single { ResourceTagsRepository<Tag> { value } }

        single { AcmRequestHandler(get()) }
        single { JsonRequestFactory(get(), "com.amazonaws.services.certificatemanager.model") }

        single { xmlMapper() }
        single { objectMapper() }
    }, contentConvertersModule())
}
