package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem

interface PrefixListMixin {
    @ListItem("cidrSet")
    fun getCidrs(): List<String>
}
