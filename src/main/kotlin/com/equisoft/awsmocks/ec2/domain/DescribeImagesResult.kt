package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.DescribeImagesResult
import com.amazonaws.services.ec2.model.Filter

fun DescribeImagesResult.filter(filters: List<Filter>): DescribeImagesResult {
    images.filter { image ->
        filters.any { filter ->
            val testValue: String = when (filter.name) {
                "name" -> image.name
                "architecture" -> image.architecture
                "description" -> image.description
                "hypervisor" -> image.hypervisor
                "image-id" -> image.imageId
                "image-type" -> image.imageType
                "owner-alias" -> image.imageOwnerAlias
                "owner-id" -> image.ownerId
                "platform" -> image.platform
                else -> ""
            }
            filter.matches(testValue)
        }
    }.distinct().also(this::setImages)

    return this
}
