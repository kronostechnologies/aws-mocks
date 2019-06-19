package com.equisoft.awsmocks.ec2.context

import com.amazonaws.services.ec2.model.AccountAttribute
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupEgressResult
import com.amazonaws.services.ec2.model.DescribeAccountAttributesResult
import com.amazonaws.services.ec2.model.DescribePrefixListsResult
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
import com.amazonaws.services.ec2.model.DescribeVpcEndpointServicesResult
import com.amazonaws.services.ec2.model.DescribeVpcEndpointsResult
import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.ModifyVpcEndpointResult
import com.amazonaws.services.ec2.model.PrefixList
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.ec2.model.ServiceDetail
import com.amazonaws.services.ec2.model.Tag
import com.amazonaws.services.ec2.model.VpcEndpoint
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.FormRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.ec2.application.Ec2RequestHandler
import com.equisoft.awsmocks.ec2.application.SecurityGroupService
import com.equisoft.awsmocks.ec2.application.VpcService
import com.equisoft.awsmocks.ec2.infrastructure.persistence.SecurityGroupRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.ServiceDetailRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcEndpointRepository
import com.equisoft.awsmocks.ec2.interfaces.http.Ec2ParametersDeserializer
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.CustomJacksonAnnotationIntrospector
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.AccountAttributeMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.AuthorizeSecurityGroupEgressMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.DescribeAccountAttributesMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.DescribePrefixListsMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.DescribeSecurityGroupsMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.DescribeVpcEndpointServicesMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.DescribeVpcEndpointsMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.IpPermissionMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.ModifyVpcEndpointMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.PrefixListMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.SecurityGroupMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.ServiceDetailMixin
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.VpcEndpointMixin
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import org.koin.core.module.Module
import org.koin.dsl.module

@SuppressWarnings("LongMethod")
fun ec2Modules(): List<Module> {
    return listOf(module {
        single(createdAtStart = true) { Ec2Bootstrapper(get(), get()) }

        single { VpcService(get(), get()) }
        single { SecurityGroupService(get()) }

        single { VpcEndpointRepository() }
        single { ServiceDetailRepository() }
        single { SecurityGroupRepository() }
        single { ResourceTagsRepository<Tag> { value } }

        single { Ec2RequestHandler(get(), get()) }
        single<ParametersDeserializer> { Ec2ParametersDeserializer() }
        single { FormRequestFactory(get(), "com.amazonaws.services.ec2.model") }

        single {
            xmlMapper()
                .addMixIns()
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .setAnnotationIntrospector(AnnotationIntrospector.pair(
                    CustomJacksonAnnotationIntrospector(), JaxbAnnotationIntrospector(TypeFactory.defaultInstance()))
                ) as XmlMapper
        }
        single { objectMapper() }
    }, contentConvertersModule())
}

private fun XmlMapper.addMixIns(): XmlMapper = addMixIn(AccountAttribute::class.java, AccountAttributeMixin::class.java)
    .addMixIn(AuthorizeSecurityGroupEgressResult::class.java, AuthorizeSecurityGroupEgressMixin::class.java)
    .addMixIn(DescribeAccountAttributesResult::class.java, DescribeAccountAttributesMixin::class.java)
    .addMixIn(DescribePrefixListsResult::class.java, DescribePrefixListsMixin::class.java)
    .addMixIn(DescribeSecurityGroupsResult::class.java, DescribeSecurityGroupsMixin::class.java)
    .addMixIn(DescribeVpcEndpointsResult::class.java, DescribeVpcEndpointsMixin::class.java)
    .addMixIn(DescribeVpcEndpointServicesResult::class.java, DescribeVpcEndpointServicesMixin::class.java)
    .addMixIn(IpPermission::class.java, IpPermissionMixin::class.java)
    .addMixIn(ModifyVpcEndpointResult::class.java, ModifyVpcEndpointMixin::class.java)
    .addMixIn(PrefixList::class.java, PrefixListMixin::class.java)
    .addMixIn(SecurityGroup::class.java, SecurityGroupMixin::class.java)
    .addMixIn(ServiceDetail::class.java, ServiceDetailMixin::class.java)
    .addMixIn(VpcEndpoint::class.java, VpcEndpointMixin::class.java)
    as XmlMapper
