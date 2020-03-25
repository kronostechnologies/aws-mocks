package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface SubnetMixin {
    @ListItem("tagSet")
    fun getTags(): List<Tag>
}
