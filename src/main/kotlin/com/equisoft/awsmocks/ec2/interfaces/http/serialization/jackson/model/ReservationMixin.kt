package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.GroupIdentifier
import com.amazonaws.services.ec2.model.Instance
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem

interface ReservationMixin {
    @ListItem("groupSet")
    fun getGroups(): List<GroupIdentifier>

    @ListItem("instancesSet")
    fun getInstances(): List<Instance>
}
