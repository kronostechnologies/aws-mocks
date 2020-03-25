package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.VpcEndpoint

fun List<VpcEndpoint>.applyPrefixListFilters(filters: List<Filter>): List<VpcEndpoint> = filter { vpcEndpoint ->
    filters.any { filter ->
        when (filter.name) {
            "prefix-list-id" -> vpcEndpoint.vpcEndpointId in filter.values
            "prefix-list-name" -> vpcEndpoint.serviceName in filter.values
            else -> false
        }
    }
}
