@file:SuppressWarnings("MatchingDeclarationName")

package com.equisoft.awsmocks.route53.context

import com.amazonaws.services.route53.model.DelegationSet
import com.amazonaws.services.route53.model.GetHostedZoneResult
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
import com.equisoft.awsmocks.route53.application.DelegationSetService
import com.equisoft.awsmocks.route53.application.HostedZoneService
import com.equisoft.awsmocks.route53.application.Route53RequestHandler
import com.equisoft.awsmocks.route53.application.VpcAssociationService
import com.equisoft.awsmocks.route53.infrastructure.persistence.DelegationSetAssociationRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.DelegationSetRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.HostedZoneRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.RecordSetRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.VpcAssociationRepository
import com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model.DelegationSetMixin
import com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model.GetHostedZoneResultMixin
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

        single { DelegationSetService(get(), get()) }
        single { HostedZoneService(get(), get(), get(), get()) }
        single { VpcAssociationService(get()) }

        single { DelegationSetRepository() }
        single { DelegationSetAssociationRepository() }
        single { HostedZoneRepository() }
        single { RecordSetRepository() }
        single { ResourceTagsRepository<Tag> { value } }
        single { VpcAssociationRepository() }

        single { Route53RequestHandler(get(), get(), get()) }
        single { XmlRequestFactory(get(), "com.amazonaws.services.route53.model") }

        single { xmlMapper().addMixIns() }
        single { objectMapper() }
    }, contentConvertersModule(), configModule())
}

private fun XmlMapper.addMixIns(): XmlMapper = this
    .addMixIn(DelegationSet::class.java, DelegationSetMixin::class.java)
    .addMixIn(GetHostedZoneResult::class.java, GetHostedZoneResultMixin::class.java)
    .addMixIn(ListHostedZonesResult::class.java, ListHostedZonesMixin::class.java)
    .addMixIn(ListResourceRecordSetsResult::class.java, ListResourceRecordSetsMixin::class.java)
    .addMixIn(ResourceRecordSet::class.java, ResourceRecordSetMixin::class.java)
    .addMixIn(ResourceTagSet::class.java, ResourceTagSetMixin::class.java)
    as XmlMapper
