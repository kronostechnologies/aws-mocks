package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.Action
import com.amazonaws.services.elasticloadbalancingv2.model.RuleCondition
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface RuleMixin {
    @ListMember
    fun getConditions(): List<RuleCondition>

    @ListMember
    fun getActions(): List<Action>
}
