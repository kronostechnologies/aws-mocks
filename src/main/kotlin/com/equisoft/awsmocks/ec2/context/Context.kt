package com.equisoft.awsmocks.ec2.context

import com.amazonaws.services.ec2.model.*
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.exceptions.Error
import com.equisoft.awsmocks.common.exceptions.ErrorResponse
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.common.interfaces.http.FormRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.ec2.application.*
import com.equisoft.awsmocks.ec2.infrastructure.persistence.*
import com.equisoft.awsmocks.ec2.interfaces.http.Ec2ParametersDeserializer
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.Ec2JacksonAnnotationIntrospector
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model.*
import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

@SuppressWarnings("LongMethod")
fun ec2Modules(): List<Module> {
    return listOf(module {
        single(createdAtStart = true) { Ec2Bootstrapper(get(), get()) }

        single { InstanceService(get(), get(), get()) }
        single { InternetGatewayService(get(), get(), get()) }
        single { ReservationService(get(), get(), get(), get(), get()) }
        single { RouteTableService(get(), get()) }
        single { SecurityGroupService(get(), get()) }
        single { SubnetService(get(), get(), get()) }
        single { VolumeService(get(), get()) }
        single { VpcService(get(), get(), get()) }
        single { VpcEndpointService(get(), get(), get()) }

        single { InstanceRepository() } bind Ec2Repository::class
        single { InternetGatewayRepository() } bind Ec2Repository::class
        single { ReservationRepository() } bind Ec2Repository::class
        single { ResourceTagsRepository<Tag> { value } }
        single { RouteTableRepository() } bind Ec2Repository::class
        single { SecurityGroupRepository() } bind Ec2Repository::class
        single { ServiceDetailRepository() }
        single { SubnetRepository() } bind Ec2Repository::class
        single { VolumeRepository() } bind Ec2Repository::class
        single { VpcRepository() } bind Ec2Repository::class
        single { VpcAttributesRepository() }
        single { VpcEndpointRepository() } bind Ec2Repository::class

        single { ResourcesRepository(getAll()) }

        single {
            Ec2RequestHandler(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
        }

        single<ParametersDeserializer> { Ec2ParametersDeserializer() }
        single { FormRequestFactory(get(), "com.amazonaws.services.ec2.model") }

        single {
            xmlMapper()
                .addMixIns()
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .setAnnotationIntrospector(AnnotationIntrospector.pair(
                    Ec2JacksonAnnotationIntrospector(), JaxbAnnotationIntrospector(TypeFactory.defaultInstance()))
                ) as XmlMapper
        }
        single { objectMapper() }
    }, contentConvertersModule())
}

private fun XmlMapper.addMixIns(): XmlMapper = this
    .addMixIn(AccountAttribute::class.java, AccountAttributeMixin::class.java)
    .addMixIn(AssociateRouteTableResult::class.java, AssociateRouteTableResultMixin::class.java)
    .addMixIn(AttachInternetGatewayResult::class.java, AttachInternetGatewayResultMixin::class.java)
    .addMixIn(AuthorizeSecurityGroupEgressResult::class.java, AuthorizeSecurityGroupEgressMixin::class.java)
    .addMixIn(AuthorizeSecurityGroupIngressResult::class.java, AuthorizeSecurityGroupIngressResultMixin::class.java)
    .addMixIn(CreateRouteResult::class.java, CreateRouteResultMixin::class.java)
    .addMixIn(CreateRouteTableResult::class.java, CreateRouteTableResultMixin::class.java)
    .addMixIn(CreateInternetGatewayResult::class.java, CreateInternetGatewayResultMixin::class.java)
    .addMixIn(CreateSubnetResult::class.java, CreateSubnetResultMixin::class.java)
    .addMixIn(CreateVpcResult::class.java, CreateVpcResultMixin::class.java)
    .addMixIn(DescribeAccountAttributesResult::class.java, DescribeAccountAttributesMixin::class.java)
    .addMixIn(DescribeInstanceCreditSpecificationsResult::class.java,
        DescribeInstanceCreditSpecificationsResultMixin::class.java)
    .addMixIn(DescribeImagesResult::class.java, DescribeImagesResultMixin::class.java)
    .addMixIn(DescribeInstancesResult::class.java, DescribeInstancesResultMixin::class.java)
    .addMixIn(DescribeInternetGatewaysResult::class.java, DescribeInternetGatewaysResultMixin::class.java)
    .addMixIn(DescribePrefixListsResult::class.java, DescribePrefixListsMixin::class.java)
    .addMixIn(DescribeRouteTablesResult::class.java, DescribeRouteTablesResultMixin::class.java)
    .addMixIn(DescribeSecurityGroupsResult::class.java, DescribeSecurityGroupsMixin::class.java)
    .addMixIn(DescribeSubnetsResult::class.java, DescribeSubnetsResultMixin::class.java)
    .addMixIn(DescribeTagsResult::class.java, DescribeTagsResultMixin::class.java)
    .addMixIn(DescribeVolumesResult::class.java, DescribeVolumesResultMixin::class.java)
    .addMixIn(DescribeVpcAttributeResult::class.java, DescribeVpcAttributeResultMixin::class.java)
    .addMixIn(DescribeVpcEndpointsResult::class.java, DescribeVpcEndpointsMixin::class.java)
    .addMixIn(DescribeVpcEndpointServicesResult::class.java, DescribeVpcEndpointServicesMixin::class.java)
    .addMixIn(DescribeVpcsResult::class.java, DescribeVpcsResultMixin::class.java)
    .addMixIn(DisassociateRouteTableResult::class.java, DisassociateRouteTableResultMixin::class.java)
    .addMixIn(Instance::class.java, InstanceMixin::class.java)
    .addMixIn(InstanceNetworkInterface::class.java, InstanceNetworkInterfaceMixin::class.java)
    .addMixIn(InternetGateway::class.java, InternetGatewayMixin::class.java)
    .addMixIn(IpPermission::class.java, IpPermissionMixin::class.java)
    .addMixIn(ModifyInstanceAttributeResult::class.java, ModifyInstanceAttributeResultMixin::class.java)
    .addMixIn(ModifyVpcAttributeResult::class.java, ModifyVpcAttributeResultMixin::class.java)
    .addMixIn(ModifyVpcEndpointResult::class.java, ModifyVpcEndpointMixin::class.java)
    .addMixIn(PrefixList::class.java, PrefixListMixin::class.java)
    .addMixIn(Reservation::class.java, ReservationMixin::class.java)
    .addMixIn(RevokeSecurityGroupEgressResult::class.java, RevokeSecurityGroupEgressResultMixin::class.java)
    .addMixIn(RouteTable::class.java, RouteTableMixin::class.java)
    .addMixIn(SecurityGroup::class.java, SecurityGroupMixin::class.java)
    .addMixIn(ServiceDetail::class.java, ServiceDetailMixin::class.java)
    .addMixIn(Subnet::class.java, SubnetMixin::class.java)
    .addMixIn(Vpc::class.java, VpcMixin::class.java)
    .addMixIn(VpcEndpoint::class.java, VpcEndpointMixin::class.java)
    .addMixIn(ErrorResponse::class.java, ErrorResponseMixin::class.java)
    .addMixIn(Error::class.java, ErrorMixin::class.java)
    as XmlMapper
