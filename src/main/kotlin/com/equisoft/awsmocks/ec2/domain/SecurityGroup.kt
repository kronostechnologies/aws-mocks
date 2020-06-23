package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.SecurityGroup

fun List<SecurityGroup>.applyFilters(filters: List<Filter>): List<SecurityGroup> = filter { routeTable ->
    filters.any { filter ->
        val testValue: String = extractValueForFilter(filter, routeTable)
        filter.matches(testValue)
    }
}.distinct()

private fun extractValueForFilter(filter: Filter, securityGroup: SecurityGroup): String =
    when (filter.name) {
        "description" -> securityGroup.description
        "egress.ip-permission.cidr" -> securityGroup.ipPermissionsEgress.joinToString {
            it.ipv4Ranges.joinToString { range -> range.cidrIp }
        }
        "egress.ip-permission.from-port" -> securityGroup.ipPermissionsEgress.joinToString { it.fromPort.toString() }
        "egress.ip-permission.group-id" -> securityGroup.ipPermissionsEgress.joinToString {
            it.userIdGroupPairs.joinToString { group -> group.groupId }
        }
        "egress.ip-permission.group-name" -> securityGroup.ipPermissionsEgress.joinToString {
            it.userIdGroupPairs.joinToString { group -> group.groupName }
        }
        "egress.ip-permission.ipv6-cidr" -> securityGroup.ipPermissionsEgress.joinToString {
            it.ipv6Ranges.joinToString { range -> range.cidrIpv6 }
        }
        "egress.ip-permission.prefix-list-id" -> securityGroup.ipPermissionsEgress.joinToString {
            it.prefixListIds.joinToString { list -> list.prefixListId }
        }
        "egress.ip-permission.protocol" -> securityGroup.ipPermissionsEgress.joinToString { it.ipProtocol }
        "egress.ip-permission.to-port" -> securityGroup.ipPermissionsEgress.joinToString { it.toPort.toString() }
        "egress.ip-permission.user-id" -> securityGroup.ipPermissionsEgress.joinToString {
            it.userIdGroupPairs.joinToString { group -> group.userId }
        }
        "group-id" -> securityGroup.groupId
        "group-name" -> securityGroup.groupName
        "ip-permission.cidr" -> securityGroup.ipPermissions.joinToString {
            it.ipv4Ranges.joinToString { range -> range.cidrIp }
        }
        "ip-permission.from-port" -> securityGroup.ipPermissions.joinToString { it.fromPort.toString() }
        "ip-permission.group-id" -> securityGroup.ipPermissions.joinToString {
            it.userIdGroupPairs.joinToString { group -> group.groupId }
        }
        "ip-permission.group-name" -> securityGroup.ipPermissions.joinToString {
            it.userIdGroupPairs.joinToString { group -> group.groupName }
        }
        "ip-permission.ipv6-cidr" -> securityGroup.ipPermissions.joinToString {
            it.ipv6Ranges.joinToString { range -> range.cidrIpv6 }
        }
        "ip-permission.prefix-list-id" -> securityGroup.ipPermissions.joinToString {
            it.prefixListIds.joinToString { list -> list.prefixListId }
        }
        "ip-permission.protocol" -> securityGroup.ipPermissions.joinToString { it.ipProtocol }
        "ip-permission.to-port" -> securityGroup.ipPermissions.joinToString { it.toPort.toString() }
        "ip-permission.user-id" -> securityGroup.ipPermissions.joinToString {
            it.userIdGroupPairs.joinToString { group -> group.userId }
        }
        "owner-id" -> securityGroup.ownerId
        "tag-key" -> securityGroup.tags.joinToString { it.key }
        "vpc-id" -> securityGroup.vpcId
        else -> ""
    }
