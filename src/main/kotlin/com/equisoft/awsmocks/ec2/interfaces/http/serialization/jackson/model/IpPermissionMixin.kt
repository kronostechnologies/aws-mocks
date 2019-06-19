package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.IpRange
import com.amazonaws.services.ec2.model.PrefixListId
import com.amazonaws.services.ec2.model.UserIdGroupPair
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface IpPermissionMixin {
    @ListItem("ipRanges")
    fun getIpv4Ranges(): List<IpRange>

    @ListItem("ipv6Ranges")
    fun getIpv6Ranges(): List<IpRange>

    @ListItem("groups")
    fun getUserIdGroupPairs(): List<UserIdGroupPair>

    @ListItem("prefixListIds")
    fun getPrefixListIds(): List<PrefixListId>
}
