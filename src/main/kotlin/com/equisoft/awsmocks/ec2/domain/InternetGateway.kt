package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.InternetGateway
import com.amazonaws.services.ec2.model.InternetGatewayAttachment

fun List<InternetGateway>.applyFilters(filters: List<Filter>): List<InternetGateway> = filter { igw ->
    filters.any { filter ->
        val attachments = igw.attachments
        val testValue: String = extractValueForFilter(filter, attachments, igw)
        filter.matches(testValue)
    }
}.distinct()

private fun extractValueForFilter(
    filter: Filter,
    attachments: List<InternetGatewayAttachment>,
    igw: InternetGateway
): String = when (filter.name) {
    "attachment.state" -> attachments.joinToString { it.state }
    "attachment.vpc-id" -> attachments.joinToString { it.vpcId }
    "internet-gateway-id" -> igw.internetGatewayId
    "owner-id" -> igw.ownerId
    else -> ""
}
