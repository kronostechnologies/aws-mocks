package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.Listener
import com.amazonaws.services.elasticloadbalancingv2.model.Rule
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.elb.infrastructure.persistence.ListenerRuleRepository

class ListenerRuleService(
    private val listenerService: ListenerService,
    private val listenerRuleRepository: ListenerRuleRepository
) {
    fun getAllByListenerArn(listenerArn: String): List<Rule> {
        listenerService.getByArn(listenerArn)

        return listenerRuleRepository[listenerArn] ?: listOf()
    }

    fun getAll(ruleArns: List<String>): List<Rule> = listenerRuleRepository.values
        .flatten()
        .filter { ruleArns.contains(it.ruleArn) }
        .distinct()
        .apply {
            if (size != ruleArns.size) {
                throw NotFoundException("RuleNotFound")
            }
        }

    fun add(listenerArn: String, rule: Rule) {
        val listener: Listener = listenerService.getByArn(listenerArn)

        listenerRuleRepository.add(listener.listenerArn, rule)
    }
}
