package com.equisoft.awsmocks.autoscaling.context

import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult
import com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult
import com.amazonaws.services.autoscaling.model.DescribeNotificationConfigurationsResult
import com.amazonaws.services.autoscaling.model.InstanceMonitoring
import com.amazonaws.services.autoscaling.model.LaunchConfiguration
import com.amazonaws.services.autoscaling.model.Tag
import com.equisoft.awsmocks.autoscaling.application.AutoScalingGroupService
import com.equisoft.awsmocks.autoscaling.application.AutoScalingRequestHandler
import com.equisoft.awsmocks.autoscaling.application.LaunchConfigurationService
import com.equisoft.awsmocks.autoscaling.infrastructure.persistence.AutoScalingGroupRepository
import com.equisoft.awsmocks.autoscaling.infrastructure.persistence.LaunchConfigurationRepository
import com.equisoft.awsmocks.autoscaling.infrastructure.persistence.NotificationConfigurationRepository
import com.equisoft.awsmocks.autoscaling.interfaces.http.AutoScalingParametersDeserializer
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.AutoScalingGroupMixin
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.DescribeAutoScalingGroupsResultMixin
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.DescribeLaunchConfigurationsResultMixin
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.DescribeNotificationConfigurationsResultMixin
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.InstanceMonitoringMixin
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.LaunchConfigurationMixin
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.FormRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import org.koin.core.module.Module
import org.koin.dsl.module

fun autoScalingModules(): List<Module> = listOf(module {
    single { AutoScalingGroupService(get(), get()) }
    single { LaunchConfigurationService(get(), get()) }

    single { AutoScalingGroupRepository() }
    single { LaunchConfigurationRepository() }
    single { NotificationConfigurationRepository() }
    single { ResourceTagsRepository<Tag> { value } }

    single<ParametersDeserializer> { AutoScalingParametersDeserializer() }
    single { AutoScalingRequestHandler(get(), get(), get()) }

    single { FormRequestFactory(get(), "com.amazonaws.services.autoscaling.model") }

    single {
        xmlMapper()
            .addAutoScalingMixIns()
            .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
            .setAnnotationIntrospector(AnnotationIntrospector.pair(
                JacksonAnnotationIntrospector(), JaxbAnnotationIntrospector(TypeFactory.defaultInstance())))
            as XmlMapper
    }
    single { objectMapper() }
}, contentConvertersModule())

internal fun XmlMapper.addAutoScalingMixIns(): XmlMapper = this
    .addMixIn(AutoScalingGroup::class.java, AutoScalingGroupMixin::class.java)
    .addMixIn(DescribeAutoScalingGroupsResult::class.java, DescribeAutoScalingGroupsResultMixin::class.java)
    .addMixIn(DescribeLaunchConfigurationsResult::class.java, DescribeLaunchConfigurationsResultMixin::class.java)
    .addMixIn(DescribeNotificationConfigurationsResult::class.java,
        DescribeNotificationConfigurationsResultMixin::class.java)
    .addMixIn(InstanceMonitoring::class.java, InstanceMonitoringMixin::class.java)
    .addMixIn(LaunchConfiguration::class.java, LaunchConfigurationMixin::class.java)
    as XmlMapper
