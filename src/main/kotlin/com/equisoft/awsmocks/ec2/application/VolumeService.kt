package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Tag
import com.amazonaws.services.ec2.model.Volume
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.domain.createDefaultRootVolume
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VolumeRepository

class VolumeService(
    volumeRepository: VolumeRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<Volume, VolumeRepository>({ it.volumeId }, volumeRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidVolumeID.NotFound"

    override fun withTags(value: Volume, tags: Collection<Tag>): Volume = value.withTags(tags)

    fun ensureDefaultVolumeForInstance(instanceId: String) {
        if (repository.findRootVolume(instanceId) == null) {
            addOrUpdate(createDefaultRootVolume(instanceId))
        }
    }
}
