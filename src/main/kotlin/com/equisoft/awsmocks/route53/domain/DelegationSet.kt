package com.equisoft.awsmocks.route53.domain

import com.amazonaws.services.route53.model.DelegationSet

private const val SOME_NAME_SERVER = "some.nameserver.com"

fun newDefaultDelegationSet(id: String): DelegationSet = DelegationSet()
    .withId(id)
    .withNameServers(SOME_NAME_SERVER)
