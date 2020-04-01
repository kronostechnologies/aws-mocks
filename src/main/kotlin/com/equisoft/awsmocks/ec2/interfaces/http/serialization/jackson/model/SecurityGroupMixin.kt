package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import com.fasterxml.jackson.annotation.JsonProperty

interface SecurityGroupMixin {
    @ListItem("ipPermissions")
    fun getIpPermissions(): List<IpPermission>

    @ListItem("ipPermissionsEgress")
    fun getIpPermissionsEgress(): List<IpPermission>

    @ListItem("tagSet")
    fun getTags(): List<Tag>

    @JsonProperty("groupDescription")
    fun getDescription(): String
}
