package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.GroupIdentifier
import com.amazonaws.services.ec2.model.InstanceNetworkInterface
import com.amazonaws.services.ec2.model.InstanceState
import com.amazonaws.services.ec2.model.LicenseConfiguration
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem
import com.fasterxml.jackson.annotation.JsonProperty

interface InstanceMixin {
    @ListItem("groupSet")
    fun getSecurityGroups(): List<GroupIdentifier>

    @ListItem("licenseSet")
    fun getLicenses(): List<LicenseConfiguration>

    @ListItem("networkInterfaceSet")
    fun getNetworkInterfaces(): List<InstanceNetworkInterface>

    @ListItem("tagSet")
    fun getTags(): List<Tag>

    @JsonProperty("instanceState")
    fun getState(): InstanceState
}
