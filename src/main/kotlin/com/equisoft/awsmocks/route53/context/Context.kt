@file:SuppressWarnings("MatchingDeclarationName")

package com.equisoft.awsmocks.route53.context

import com.amazonaws.services.route53.model.ListHostedZonesResult
import com.amazonaws.services.route53.model.ListResourceRecordSetsResult
import com.amazonaws.services.route53.model.ResourceRecordSet
import com.amazonaws.services.route53.model.ResourceTagSet
import com.amazonaws.services.route53.model.Tag
import com.equisoft.awsmocks.common.context.configModule
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.XmlRequestFactory
import com.equisoft.awsmocks.route53.application.HostedZoneService
import com.equisoft.awsmocks.route53.application.Route53RequestHandler
import com.equisoft.awsmocks.route53.infrastructure.persistence.HostedZoneRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.RecordSetRepository
import com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model.ListHostedZonesMixin
import com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model.ListResourceRecordSetsMixin
import com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model.ResourceRecordSetMixin
import com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model.ResourceTagSetMixin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.koin.core.module.Module
import org.koin.dsl.module

fun route53modules(): List<Module> {
    return listOf(module {
        single(createdAtStart = true) { Route53Bootstrapper(get(), get()) }

        single { HostedZoneService(get(), get(), get()) }

        single { ResourceTagsRepository<Tag> { value } }
        single { HostedZoneRepository() }
        single { RecordSetRepository() }

        single { Route53RequestHandler(get()) }
        single { XmlRequestFactory(get(), "com.amazonaws.services.route53.model") }

        single { xmlMapper().addMixIns() }
        single { objectMapper() }
    }, contentConvertersModule(), configModule())
}

private fun XmlMapper.addMixIns(): XmlMapper = addMixIn(ResourceTagSet::class.java, ResourceTagSetMixin::class.java)
    .addMixIn(ListHostedZonesResult::class.java, ListHostedZonesMixin::class.java)
    .addMixIn(ListResourceRecordSetsResult::class.java, ListResourceRecordSetsMixin::class.java)
    .addMixIn(ResourceRecordSet::class.java, ResourceRecordSetMixin::class.java)
    as XmlMapper
