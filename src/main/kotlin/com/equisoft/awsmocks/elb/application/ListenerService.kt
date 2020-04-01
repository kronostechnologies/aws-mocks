package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.Listener
import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer
import com.amazonaws.services.elasticloadbalancingv2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.elb.infrastructure.persistence.ListenerRepository

class ListenerService(
    private val loadBalancerService: LoadBalancerService,
    listenerRepository: ListenerRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseElbService<Listener, ListenerRepository>(
    { it.listenerArn }, listenerRepository, tagsRepository) {
    override val duplicateMessage: String = "DuplicateListener"
    override val notFoundMessage: String = "ListenerNotFound"

    override fun getArn(value: Listener): String = value.listenerArn

    fun getAllByLoadBalancerArn(loadBalancerArn: String): List<Listener> {
        val loadBalancer: LoadBalancer = loadBalancerService.getByArn(loadBalancerArn)

        return repository.values.filter { it.loadBalancerArn == loadBalancer.loadBalancerArn }
    }

    fun getAllByArn(listenerArns: List<String>): List<Listener> = listenerArns.map(::getByArn)
}
