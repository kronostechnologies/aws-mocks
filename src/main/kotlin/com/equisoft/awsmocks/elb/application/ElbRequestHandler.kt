package com.equisoft.awsmocks.elb.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.elasticloadbalancingv2.model.*
import com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model.*

@SuppressWarnings("LongMethod")
class ElbRequestHandler(
    private val loadBalancerAttributesService: LoadBalancerAttributesService,
    private val targetGroupAttributesService: TargetGroupAttributesService,
    private val listenerRuleService: ListenerRuleService,
    private val listenerService: ListenerService,
    private val loadBalancerService: LoadBalancerService,
    private val targetGroupService: TargetGroupService,
    private val tagsService: TagsService
) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> =
        when (request) {
            is AddTagsRequest -> {
                tagsService.addTags(request.resourceArns, request.tags)

                AddTagsResult()
            }
            is CreateListenerRequest -> {
                val listener: Listener = createListenerFromRequest(request)
                listenerService.add(listener)

                CreateListenerResponse(CreateListenerResult().withListeners(listener))
            }
            is CreateLoadBalancerRequest -> {
                val loadBalancer: LoadBalancer = createLoadBalancerFromRequest(request)
                loadBalancerService.add(loadBalancer)

                CreateLoadBalancerResponse(CreateLoadBalancerResult().withLoadBalancers(loadBalancer))
            }
            is CreateRuleRequest -> {
                val rule: Rule = createRuleFromRequest(request)
                listenerRuleService.add(request.listenerArn, rule)

                CreateRuleResponse(CreateRuleResult().withRules(rule))
            }
            is CreateTargetGroupRequest -> {
                val targetGroup: TargetGroup = createTargetGroupFromRequest(request)
                targetGroupService.add(targetGroup)

                CreateTargetGroupResponse(CreateTargetGroupResult().withTargetGroups(targetGroup))
            }
            is DescribeListenersRequest -> {
                val listeners: List<Listener> = if (!request.listenerArns.isNullOrEmpty()) {
                    listenerService.getAllByArn(request.listenerArns)
                } else {
                    listenerService.getAllByLoadBalancerArn(request.loadBalancerArn)
                }

                DescribeListenersResponse(DescribeListenersResult().withListeners(listeners))
            }
            is DescribeLoadBalancerAttributesRequest -> {
                val attributes: List<LoadBalancerAttribute> =
                    loadBalancerAttributesService.getByArn(request.loadBalancerArn)

                DescribeLoadBalancerAttributesResponse(
                    DescribeLoadBalancerAttributesResult().withAttributes(attributes)
                )
            }
            is DescribeLoadBalancersRequest -> {
                val loadBalancers: List<LoadBalancer> = if (!request.names.isNullOrEmpty()) {
                    loadBalancerService.getAll(request.names)
                } else {
                    loadBalancerService.getAllByArn(request.loadBalancerArns)
                }

                DescribeLoadBalancersResponse(DescribeLoadBalancersResult().withLoadBalancers(loadBalancers))
            }
            is DescribeRulesRequest -> {
                val rules: List<Rule> = if (!request.ruleArns.isNullOrEmpty()) {
                    listenerRuleService.getAll(request.ruleArns)
                } else {
                    listenerRuleService.getAllByListenerArn(request.listenerArn)
                }

                DescribeRulesResponse(DescribeRulesResult().withRules(rules))
            }
            is DescribeTargetGroupAttributesRequest -> {
                val attributes: List<TargetGroupAttribute> =
                    targetGroupAttributesService.getByArn(request.targetGroupArn)

                DescribeTargetGroupAttributesResponse(DescribeTargetGroupAttributesResult().withAttributes(attributes))
            }
            is DescribeTagsRequest -> {
                val tagDescriptions: List<TagDescription> = tagsService.getTags(request.resourceArns)

                DescribeTagsResponse(DescribeTagsResult().withTagDescriptions(tagDescriptions))
            }
            is DescribeTargetGroupsRequest -> {
                val targetGroups: List<TargetGroup> = if (request.loadBalancerArn != null) {
                    targetGroupService.getAllByLoadBalancerArn(request.loadBalancerArn)
                } else if (!request.names.isNullOrEmpty()) {
                    targetGroupService.getAll(request.names)
                } else {
                    targetGroupService.getAllByArn(request.targetGroupArns)
                }

                DescribeTargetGroupsResponse(DescribeTargetGroupsResult().withTargetGroups(targetGroups))
            }
            is ModifyLoadBalancerAttributesRequest -> {
                loadBalancerAttributesService.modifyAttributes(request.loadBalancerArn, request.attributes)

                ModifyLoadBalancerAttributesResponse(
                    ModifyLoadBalancerAttributesResult().withAttributes(request.attributes)
                )
            }
            is ModifyTargetGroupAttributesRequest -> {
                targetGroupAttributesService.modifyAttributes(request.targetGroupArn, request.attributes)

                ModifyTargetGroupAttributesResponse(
                    ModifyTargetGroupAttributesResult().withAttributes(request.attributes)
                )
            }
            is ModifyTargetGroupRequest -> {
                targetGroupService.modify(request.targetGroupArn, request)

                ModifyTargetGroupResponse(ModifyTargetGroupResult().withTargetGroups())
            }
            is SetSecurityGroupsRequest -> {
                loadBalancerService.setSecurityGroups(request.loadBalancerArn, request.securityGroups)

                SetSecurityGroupsResponse(SetSecurityGroupsResult().withSecurityGroupIds(request.securityGroups))
            }
            else -> throw IllegalArgumentException(request::class.qualifiedName)
        }
}
