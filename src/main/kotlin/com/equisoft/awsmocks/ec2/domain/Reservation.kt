package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation

fun List<Reservation>.applyFilters(filters: List<Filter>): List<Reservation> = filter { reservation ->
    filters.any { filter ->
        val instances: MutableList<Instance> = reservation.instances
        val testValue: String = extractValueForFilter(filter, instances, reservation)
        filter.matches(testValue)
    }
}.distinct()

private fun extractValueForFilter(filter: Filter, instances: MutableList<Instance>, reservation: Reservation): String =
    when (filter.name) {
        "client-token" -> instances.joinToString { it.clientToken }
        "dns-name" -> instances.joinToString { it.publicDnsName }
        "group-id" -> reservation.groups.joinToString { it.groupId }
        "group-name" -> reservation.groups.joinToString { it.groupName }
        "image-id" -> instances.joinToString { it.imageId }
        "instance-id" -> instances.joinToString { it.instanceId }
        "instance-type" -> instances.joinToString { it.instanceType }
        "ip-address" -> instances.joinToString { it.publicIpAddress }
        "owner-id" -> reservation.ownerId
        "private-dns-name" -> instances.joinToString { it.privateDnsName }
        "private-ip-address" -> instances.joinToString { it.privateIpAddress }
        "reservation-id" -> reservation.reservationId
        "subnet-id" -> instances.joinToString { it.subnetId }
        else -> ""
    }
