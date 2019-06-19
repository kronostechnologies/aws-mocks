package com.equisoft.awsmocks.ec2.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.CreateVpcEndpointRequest
import com.amazonaws.services.ec2.model.DescribeAccountAttributesRequest
import com.amazonaws.services.ec2.model.DescribePrefixListsRequest
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest
import com.amazonaws.services.ec2.model.DescribeVpcEndpointServicesRequest
import com.amazonaws.services.ec2.model.DescribeVpcEndpointsRequest
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.ModifyVpcEndpointRequest
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.common.interfaces.http.toList
import com.equisoft.awsmocks.common.interfaces.http.toMap
import io.ktor.http.Parameters

class Ec2ParametersDeserializer : ParametersDeserializer {
    @Suppress("UNCHECKED_CAST", "LongMethod")
    override fun addParameters(parameters: Parameters, request: AmazonWebServiceRequest): AmazonWebServiceRequest =
        when (request) {
            is CreateVpcEndpointRequest -> {
                request.withClientToken(parameters["ClientToken"])
                    .withPolicyDocument(parameters["PolicyDocument"])
                    .withPrivateDnsEnabled(parameters["PrivateDnsEnabled"]?.toBoolean())
                    .withServiceName(parameters["ServiceName"])
                    .withVpcEndpointType(parameters["VpcEndpointType"])
                    .withVpcId(parameters["VpcId"])
            }
            is CreateSecurityGroupRequest -> {
                request.withGroupName(parameters["GroupName"])
                    .withDescription(parameters["GroupDescription"])
                    .withVpcId(parameters["VpcId"])
            }
            is DescribeAccountAttributesRequest -> {
                request.withAttributeNames(parameters.toList("AttributeName"))
            }
            is DescribePrefixListsRequest -> {
                request.withFilters(parameters.getFilters())
            }
            is DescribeSecurityGroupsRequest -> {
                request.withFilters(parameters.getFilters())
                    .withGroupIds(parameters.toList("GroupId"))
                    .withGroupNames(parameters.toList("GroupName"))
            }
            is DescribeVpcEndpointsRequest -> {
                request.withVpcEndpointIds(parameters.toList("VpcEndpointId"))
            }
            is DescribeVpcEndpointServicesRequest -> {
                request.withServiceNames(parameters.toList("ServiceName"))
            }
            is ModifyVpcEndpointRequest -> {
                request.withVpcEndpointId(parameters["VpcEndpointId"])
                    .withPolicyDocument(parameters["PolicyDocument"])
                    .withResetPolicy(parameters["ResetPolicy"]?.toBoolean())
                    .withAddRouteTableIds(parameters.toList("AddRouteTableId"))
                    .withAddSecurityGroupIds(parameters.toList("AddSecurityGroupId"))
                    .withAddSubnetIds(parameters.toList("AddSubnetId"))
                    .withRemoveRouteTableIds(parameters.toList("RemoveRouteTableId"))
                    .withRemoveSecurityGroupIds(parameters.toList("RemoveSecurityGroupId"))
                    .withRemoveSubnetIds(parameters.toList("RemoveSubnetId"))
            }
            else -> request
        }
}

private fun Parameters.getFilters(): List<Filter> = toMap("Filter", "Name", "Value")
    .map { Filter().withName(it.key).withValues(it.value) }
