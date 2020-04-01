package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.GroupIdentifier
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface InstanceNetworkInterfaceMixin {
    @ListItem("groupSet")
    fun getGroups(): List<GroupIdentifier>
}
