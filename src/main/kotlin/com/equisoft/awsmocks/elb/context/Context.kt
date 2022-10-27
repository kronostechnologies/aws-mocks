package com.equisoft.awsmocks.elb.context

import com.amazonaws.services.elasticloadbalancingv2.model.CreateListenerResult
import com.amazonaws.services.elasticloadbalancingv2.model.CreateLoadBalancerResult
import com.amazonaws.services.elasticloadbalancingv2.model.CreateRuleResult
import com.amazonaws.services.elasticloadbalancingv2.model.CreateTargetGroupResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeListenersResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancerAttributesResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeRulesResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeTagsResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeTargetGroupAttributesResult
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeTargetGroupsResult
import com.amazonaws.services.elasticloadbalancingv2.model.HostHeaderConditionConfig
import com.amazonaws.services.elasticloadbalancingv2.model.HttpHeaderConditionConfig
import com.amazonaws.services.elasticloadbalancingv2.model.HttpRequestMethodConditionConfig
import com.amazonaws.services.elasticloadbalancingv2.model.Listener
import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyLoadBalancerAttributesResult
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyTargetGroupAttributesResult
import com.amazonaws.services.elasticloadbalancingv2.model.ModifyTargetGroupResult
import com.amazonaws.services.elasticloadbalancingv2.model.PathPatternConditionConfig
import com.amazonaws.services.elasticloadbalancingv2.model.QueryStringConditionConfig
import com.amazonaws.services.elasticloadbalancingv2.model.QueryStringKeyValuePair
import com.amazonaws.services.elasticloadbalancingv2.model.Rule
import com.amazonaws.services.elasticloadbalancingv2.model.SetSecurityGroupsResult
import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.amazonaws.services.elasticloadbalancingv2.model.TagDescription
import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroup
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.exceptions.ErrorResponse
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.FormRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.elb.application.ElbRequestHandler
import com.equisoft.awsmocks.elb.application.ListenerRuleService
import com.equisoft.awsmocks.elb.application.ListenerService
import com.equisoft.awsmocks.elb.application.LoadBalancerAttributesService
import com.equisoft.awsmocks.elb.application.LoadBalancerService
import com.equisoft.awsmocks.elb.application.TagsService
import com.equisoft.awsmocks.elb.application.TargetGroupAttributesService
import com.equisoft.awsmocks.elb.application.TargetGroupService
import com.equisoft.awsmocks.elb.infrastructure.persistence.ListenerRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.ListenerRuleRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.LoadBalancerAttributesRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.LoadBalancerRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.TargetGroupAttributesRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.TargetGroupRepository
import com.equisoft.awsmocks.elb.interfaces.http.ElbParametersDeserializer
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.CreateListenerResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.CreateLoadBalancerResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.CreateRuleResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.CreateTargetGroupResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeListenersResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeLoadBalancerAttributesResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeLoadBalancersResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeRulesResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeTagsResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeTargetGroupAttributesResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.DescribeTargetGroupsResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.ErrorResponseMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.HostHeaderConditionConfigMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.HttpHeaderConditionConfigMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.HttpRequestMethodConditionConfigMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.ListenerMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.LoadBalancerMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.ModifyLoadBalancerAttributesResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.ModifyTargetGroupAttributesResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.ModifyTargetGroupResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.PathPatternConditionConfigMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.QueryStringConditionConfigMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.QueryStringKeyValuePairMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.RuleMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.SetSecurityGroupsResultMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.TagDescriptionMixin
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.TargetGroupMixin
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationIntrospector
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
            .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE)
            .setAnnotationIntrospector(
                AnnotationIntrospector.pair(
                    JacksonAnnotationIntrospector(), JakartaXmlBindAnnotationIntrospector(TypeFactory.defaultInstance())
                )
            )
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
