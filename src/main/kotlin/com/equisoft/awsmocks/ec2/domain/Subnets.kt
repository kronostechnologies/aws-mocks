package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Subnet

fun List<Subnet>.applyFilters(filters: List<Filter>): List<Subnet> = filter { subnet ->
    filters.any { filter ->
        val testValue: String = when (filter.name) {
            "availability-zone" -> subnet.availabilityZone
            "availability-zone-id" -> subnet.availabilityZoneId
            "cidr-block" -> subnet.cidrBlock
            "default-for-az" -> subnet.isDefaultForAz.toString()
            "owner-id" -> subnet.ownerId
            "state " -> subnet.state
            "subnet-arn" -> subnet.subnetArn
            "subnet-id" -> subnet.subnetId
            "vpc-id" -> subnet.vpcId
            else -> ""
        }
        filter.matches(testValue)
    }
}.distinct()
