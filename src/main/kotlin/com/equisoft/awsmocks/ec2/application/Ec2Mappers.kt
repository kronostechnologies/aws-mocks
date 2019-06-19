@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.CreateVpcEndpointRequest
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.ec2.model.State
import com.amazonaws.services.ec2.model.VpcEndpoint
import com.equisoft.awsmocks.common.interfaces.http.accountId
import java.util.Date
import java.util.UUID

fun vpcEndpointFromRequest(request: CreateVpcEndpointRequest): VpcEndpoint {
    return VpcEndpoint()
        .withVpcEndpointId(UUID.randomUUID().toString())
        .withVpcEndpointType(request.vpcEndpointType)
        .withCreationTimestamp(Date())
        .withVpcId(request.vpcId)
        .withPrivateDnsEnabled(request.privateDnsEnabled)
        .withPolicyDocument(request.policyDocument)
        .withServiceName(request.serviceName)
        .withRouteTableIds(request.routeTableIds)
        .withGroups()
        .withDnsEntries()
        .withNetworkInterfaceIds()
        .withState(State.Available.toString().toLowerCase())
}

fun securityGroupFromRequest(request: CreateSecurityGroupRequest): SecurityGroup = SecurityGroup()
    .withGroupId(UUID.randomUUID().toString())
    .withGroupName(request.groupName)
    .withDescription(request.description)
    .withVpcId(request.vpcId)
    .withOwnerId(request.accountId)
