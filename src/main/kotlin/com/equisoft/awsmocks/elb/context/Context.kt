package com.equisoft.awsmocks.elb.context

import com.amazonaws.services.elasticloadbalancingv2.model.*
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.exceptions.ErrorResponse
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.FormRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.elb.application.*
import com.equisoft.awsmocks.elb.infrastructure.persistence.ListenerRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.ListenerRuleRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.LoadBalancerAttributesRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.LoadBalancerRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.TargetGroupAttributesRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.TargetGroupRepository
import com.equisoft.awsmocks.elb.interfaces.http.ElbParametersDeserializer
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.*
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import org.koin.core.module.Module
import org.koin.dsl.module

fun elbModules(): List<Module> = listOf(module {
    single { ListenerRuleService(get(), get()) }
    single { ListenerService(get(), get(), get()) }
    single { LoadBalancerAttributesService(get(), get()) }
    single { LoadBalancerService(get(), get()) }
    single { TagsService(get(), get()) }
    single { TargetGroupAttributesService(get(), get()) }
    single { TargetGroupService(get(), get(), get()) }

    single { ListenerRepository() }
    single { ListenerRuleRepository() }
    single { LoadBalancerAttributesRepository() }
    single { LoadBalancerRepository() }
    single { TargetGroupAttributesRepository() }
    single { TargetGroupRepository() }
    single { ResourceTagsRepository<Tag> { value } }

    single<ParametersDeserializer> { ElbParametersDeserializer() }
    single { ElbRequestHandler(get(), get(), get(), get(), get(), get(), get()) }

    single { FormRequestFactory(get(), "com.amazonaws.services.elasticloadbalancingv2.model") }

    single {
        xmlMapper()
            .addElbMixIns()
            .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
            .setAnnotationIntrospector(AnnotationIntrospector.pair(
                JacksonAnnotationIntrospector(), JaxbAnnotationIntrospector(TypeFactory.defaultInstance())))
            as XmlMapper
    }
    single { objectMapper() }
}, contentConvertersModule())

internal fun XmlMapper.addElbMixIns(): XmlMapper = this
    .addMixIn(CreateListenerResult::class.java, CreateListenerResultMixin::class.java)
    .addMixIn(CreateLoadBalancerResult::class.java, CreateLoadBalancerResultMixin::class.java)
    .addMixIn(CreateRuleResult::class.java, CreateRuleResultMixin::class.java)
    .addMixIn(CreateTargetGroupResult::class.java, CreateTargetGroupResultMixin::class.java)
    .addMixIn(DescribeListenersResult::class.java, DescribeListenersResultMixin::class.java)
    .addMixIn(DescribeLoadBalancerAttributesResult::class.java, DescribeLoadBalancerAttributesResultMixin::class.java)
    .addMixIn(DescribeLoadBalancersResult::class.java, DescribeLoadBalancersResultMixin::class.java)
    .addMixIn(DescribeRulesResult::class.java, DescribeRulesResultMixin::class.java)
    .addMixIn(DescribeTargetGroupAttributesResult::class.java, DescribeTargetGroupAttributesResultMixin::class.java)
    .addMixIn(DescribeTagsResult::class.java, DescribeTagsResultMixin::class.java)
    .addMixIn(DescribeTargetGroupsResult::class.java, DescribeTargetGroupsResultMixin::class.java)
    .addMixIn(HostHeaderConditionConfig::class.java, HostHeaderConditionConfigMixin::class.java)
    .addMixIn(HttpHeaderConditionConfig::class.java, HttpHeaderConditionConfigMixin::class.java)
    .addMixIn(HttpRequestMethodConditionConfig::class.java, HttpRequestMethodConditionConfigMixin::class.java)
    .addMixIn(Listener::class.java, ListenerMixin::class.java)
    .addMixIn(LoadBalancer::class.java, LoadBalancerMixin::class.java)
    .addMixIn(ModifyLoadBalancerAttributesResult::class.java, ModifyLoadBalancerAttributesResultMixin::class.java)
    .addMixIn(ModifyTargetGroupAttributesResult::class.java, ModifyTargetGroupAttributesResultMixin::class.java)
    .addMixIn(ModifyTargetGroupResult::class.java, ModifyTargetGroupResultMixin::class.java)
    .addMixIn(PathPatternConditionConfig::class.java, PathPatternConditionConfigMixin::class.java)
    .addMixIn(Rule::class.java, RuleMixin::class.java)
    .addMixIn(SetSecurityGroupsResult::class.java, SetSecurityGroupsResultMixin::class.java)
    .addMixIn(TagDescription::class.java, TagDescriptionMixin::class.java)
    .addMixIn(TargetGroup::class.java, TargetGroupMixin::class.java)
    .addMixIn(QueryStringConditionConfig::class.java, QueryStringConditionConfigMixin::class.java)
    .addMixIn(QueryStringKeyValuePair::class.java, QueryStringKeyValuePairMixin::class.java)
    .addMixIn(ErrorResponse::class.java, ErrorResponseMixin::class.java)
    as XmlMapper
