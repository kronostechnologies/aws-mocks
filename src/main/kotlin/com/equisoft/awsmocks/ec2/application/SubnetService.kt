package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Subnet
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.SubnetRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcRepository

class SubnetService(
    private val vpcRepository: VpcRepository,
    subnetRepository: SubnetRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<Subnet, SubnetRepository>({ it.subnetId }, subnetRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidSubnetID.NotFound"

    override fun addOrUpdate(value: Subnet) {
        requireNotNull(vpcRepository[value.vpcId])

        super.addOrUpdate(value)
    }

    override fun withTags(value: Subnet, tags: Collection<Tag>): Subnet = value.withTags(tags)
}
