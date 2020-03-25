package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.GroupIdentifier
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.InstanceState
import com.amazonaws.services.ec2.model.ModifyInstanceAttributeRequest
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.domain.InstanceStateCodes.Companion.TERMINATED
import com.equisoft.awsmocks.ec2.infrastructure.persistence.InstanceRepository

class InstanceService(
    private val volumeService: VolumeService,
    instanceRepository: InstanceRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<Instance, InstanceRepository>({ it.instanceId }, instanceRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidInstanceID.NotFound"

    override fun addOrUpdate(value: Instance) {
        super.addOrUpdate(value)

        volumeService.ensureDefaultVolumeForInstance(value.instanceId)
        tagsRepository[value.instanceId] = value.tags
    }

    override fun withTags(value: Instance, tags: Collection<Tag>): Instance = value.withTags(tags)

    fun modifyAttributes(instanceId: String, request: ModifyInstanceAttributeRequest) {
        get(instanceId).apply {
            request.ebsOptimized?.also(::setEbsOptimized)
            request.enaSupport?.also(::setEnaSupport)
            request.groups.takeIf { it.isNotEmpty() }
                ?.map { GroupIdentifier().withGroupId(it) }
                ?.also(::setSecurityGroups)
            request.instanceType?.also(::setInstanceType)
            request.kernel?.also(::setKernelId)
            request.ramdisk?.also(::setRamdiskId)
            request.sourceDestCheck?.also(::setSourceDestCheck)
        }.also(::addOrUpdate)
    }

    fun terminateInstances(instanceIds: List<String>) {
        instanceIds.forEach {
            get(it).apply {
                state = InstanceState().withCode(TERMINATED).withName("terminated")
            }.also(::addOrUpdate)
        }
    }
}
