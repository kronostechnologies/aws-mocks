package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.amazonaws.services.elasticloadbalancingv2.model.Rule

class ListenerRuleRepository : BaseElbRepository<String, List<Rule>>() {
    @Synchronized
    fun add(listenerArn: String?, rule: Rule) {
        val rules: List<Rule> = getOrPut(listenerArn) { listOf() }
        put(listenerArn, rules + rule)
    }
}
