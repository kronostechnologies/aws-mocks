package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.InternetGatewayAttachment
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface InternetGatewayMixin {
    @ListItem("tagSet")
    fun getTags(): List<Tag>

    @ListItem("attachmentSet")
    fun getAttachments(): List<InternetGatewayAttachment>
}
