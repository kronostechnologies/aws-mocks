package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeTagsResponse")
interface DescribeTagsResultMixin {
    @ListItem("tagSet")
    fun getTags(): List<Tag>
}
