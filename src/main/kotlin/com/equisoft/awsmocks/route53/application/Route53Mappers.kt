@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.route53.application

import com.amazonaws.services.route53.model.*
import com.equisoft.awsmocks.route53.domain.newDefaultDelegationSet
import java.util.UUID

fun createDelegationSetFromRequest(request: CreateReusableDelegationSetRequest): DelegationSet =
    newDefaultDelegationSet(UUID.randomUUID().toString())
        .withCallerReference(request.callerReference)

fun createHostedZoneFromRequest(zoneRequest: CreateHostedZoneRequest): HostedZone {
    val zoneName = zoneRequest.name

    val privateZone = zoneRequest.hostedZoneConfig?.isPrivateZone ?: false
    val config = (zoneRequest.hostedZoneConfig ?: HostedZoneConfig()).withPrivateZone(privateZone)

    return HostedZone(zoneName, "$zoneName.", zoneRequest.callerReference)
        .withConfig(config)
}

fun createHostedZoneResult(hostedZone: HostedZone): CreateHostedZoneResult {
    val changeInfo = ChangeInfo().withId("/change/${hostedZone.name}").withStatus(ChangeStatus.INSYNC)
    return CreateHostedZoneResult()
        .withHostedZone(hostedZone)
        .withChangeInfo(changeInfo)
}

fun getHostedZoneResult(hostedZone: HostedZone): GetHostedZoneResult = GetHostedZoneResult().withHostedZone(hostedZone)

fun updateCommentResult(hostedZone: HostedZone): UpdateHostedZoneCommentResult =
    UpdateHostedZoneCommentResult().withHostedZone(hostedZone)
