package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Volume
import com.equisoft.awsmocks.ec2.domain.DEFAULT_ROOT_VOLUME_NAME
import com.equisoft.awsmocks.ec2.domain.applyFilters

class VolumeRepository : BaseEc2Repository<String, Volume>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<Volume> = values.toList().applyFilters(filters)

    @Synchronized
    fun findRootVolume(instanceId: String): Volume? = values.find { volume ->
        volume.attachments.any { it.instanceId == instanceId && it.device == DEFAULT_ROOT_VOLUME_NAME }
    }
}
