package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.IpPermission
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface SecurityGroupMixin {
    @ListItem("ipPermissions")
    fun getIpPermissions(): List<IpPermission>

    @ListItem("ipPermissionsEgress")
    fun getIpPermissionsEgress(): List<IpPermission>
}
