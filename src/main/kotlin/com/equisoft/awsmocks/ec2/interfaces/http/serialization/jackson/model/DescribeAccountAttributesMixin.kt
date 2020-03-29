package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.AccountAttribute
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeAccountAttributesResponse")
interface DescribeAccountAttributesMixin {
    @ListItem("accountAttributeSet")
    fun getAccountAttributes(): List<AccountAttribute>
}
