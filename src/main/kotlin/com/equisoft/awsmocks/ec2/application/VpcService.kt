package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Tag
import com.amazonaws.services.ec2.model.Vpc
import com.amazonaws.services.ec2.model.VpcAttributeName
import com.amazonaws.services.ec2.model.VpcAttributeName.EnableDnsHostnames
import com.amazonaws.services.ec2.model.VpcAttributeName.EnableDnsSupport
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcAttributesRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcRepository

class VpcService(
    vpcRepository: VpcRepository,
    tagsRepository: ResourceTagsRepository<Tag>,
    private val vpcAttributesRepository: VpcAttributesRepository
) : BaseEc2Service<Vpc, VpcRepository>({ it.vpcId }, vpcRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidVpcID.NotFound"

    override fun addOrUpdate(value: Vpc) {
        super.addOrUpdate(value)

        modifyAttributes(value.vpcId, EnableDnsSupport to true, EnableDnsHostnames to true)
    }

    fun modifyAttributes(vpcId: String, vararg attributes: Pair<VpcAttributeName, Boolean?>) {
        val vpc = getWithoutTags(vpcId)
        attributes.filter { it.second != null }.forEach { (attribute, enabled) ->
            if (enabled == true) {
                vpcAttributesRepository.add(vpc.vpcId, attribute)
            } else {
                vpcAttributesRepository.remove(vpc.vpcId, attribute)
            }
        }
    }

    fun hasAttribute(vpcId: String, attribute: VpcAttributeName): Boolean {
        val vpc: Vpc = getWithoutTags(vpcId)
        return vpcAttributesRepository[vpc.vpcId]?.contains(attribute) ?: false
    }

    override fun withTags(value: Vpc, tags: Collection<Tag>): Vpc = value.withTags(tags)
}
