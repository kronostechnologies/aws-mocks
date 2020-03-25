package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Volume
import com.amazonaws.services.ec2.model.VolumeAttachment
import com.amazonaws.services.ec2.model.VolumeAttachmentState
import com.amazonaws.services.ec2.model.VolumeState
import java.util.Date
import java.util.UUID

const val DEFAULT_ROOT_VOLUME_NAME: String = "root"

fun createDefaultRootVolume(instanceId: String): Volume {
    val volumeAttachment = VolumeAttachment().withAttachTime(Date())
        .withDeleteOnTermination(true)
        .withDevice(DEFAULT_ROOT_VOLUME_NAME)
        .withInstanceId(instanceId)
        .withState(VolumeAttachmentState.Attached)

    return Volume().withAttachments(volumeAttachment)
        .withAvailabilityZone("az")
        .withCreateTime(Date())
        .withEncrypted(false)
        .withState(VolumeState.InUse)
        .withVolumeId(UUID.randomUUID().toString())
}

fun List<Volume>.applyFilters(filters: List<Filter>): List<Volume> = filter { volume ->
    filters.any { filter ->
        val attachments = volume.attachments
        val testValue: String = extractValueForFilter(filter, attachments, volume)
        filter.matches(testValue)
    }
}.distinct()

private fun extractValueForFilter(filter: Filter, attachments: List<VolumeAttachment>, volume: Volume): String =
    when (filter.name) {
        "attachment.device" -> attachments.joinToString { it.device }
        "attachment.instance-id" -> attachments.joinToString { it.instanceId }
        "attachment.status" -> attachments.joinToString { it.state }
        "encrypted" -> volume.encrypted.toString()
        "status" -> volume.state
        "volume-id" -> volume.volumeId
        "volume-type" -> volume.volumeType
        else -> ""
    }
