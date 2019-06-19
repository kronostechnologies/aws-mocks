package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.AccountAttributeValue
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface AccountAttributeMixin {
    @ListItem("attributeValueSet")
    fun getAttributeValues(): List<AccountAttributeValue>
}
