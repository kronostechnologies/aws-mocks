@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.route53.application

import com.amazonaws.services.route53.model.ChangeInfo
import com.amazonaws.services.route53.model.ChangeStatus
import com.amazonaws.services.route53.model.CreateHostedZoneRequest
import com.amazonaws.services.route53.model.CreateHostedZoneResult
import com.amazonaws.services.route53.model.GetHostedZoneResult
import com.amazonaws.services.route53.model.HostedZone
import com.amazonaws.services.route53.model.HostedZoneConfig
import com.amazonaws.services.route53.model.UpdateHostedZoneCommentResult

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
