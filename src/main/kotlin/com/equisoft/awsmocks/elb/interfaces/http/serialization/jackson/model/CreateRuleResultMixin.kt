package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.Rule
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface CreateRuleResultMixin {
    @ListMember
    fun getRules(): List<Rule>
}
