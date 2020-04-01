package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.InternetGateway
import com.amazonaws.services.ec2.model.InternetGatewayAttachment
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.InternetGatewayRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.VpcRepository

class InternetGatewayService(
    private val vpcRepository: VpcRepository,
    internetGatewayRepository: InternetGatewayRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<InternetGateway, InternetGatewayRepository>({ it.internetGatewayId }, internetGatewayRepository,
    tagsRepository) {
    override val notFoundMessage: String = "InvalidInternetGatewayID.NotFound"

    fun attach(internetGatewayId: String, vpcId: String) {
        requireNotNull(vpcRepository[vpcId])

        val internetGateway: InternetGateway = get(internetGatewayId)
        val attachment: InternetGatewayAttachment = InternetGatewayAttachment()
            .withState("available")
            .withVpcId(vpcId)

        internetGateway.attachments.add(attachment)

        addOrUpdate(internetGateway)
    }

    override fun withTags(value: InternetGateway, tags: Collection<Tag>): InternetGateway = value.withTags(tags)
}
