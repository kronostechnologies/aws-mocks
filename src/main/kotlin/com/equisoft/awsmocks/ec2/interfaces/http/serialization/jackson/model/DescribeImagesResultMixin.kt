package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.PrefixList
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeImagesResponse")
interface DescribeImagesResultMixin {
    @ListItem("imagesSet")
    fun getImages(): List<PrefixList>
}
