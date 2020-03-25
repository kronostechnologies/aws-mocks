package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.Volume
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeVolumesResponse")
interface DescribeVolumesResultMixin {
    @ListItem("volumeSet")
    fun getVolumes(): List<Volume>
}
