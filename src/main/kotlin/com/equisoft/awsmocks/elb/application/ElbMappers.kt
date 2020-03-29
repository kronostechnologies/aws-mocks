package com.equisoft.awsmocks.elb.application

import com.amazonaws.services.elasticloadbalancingv2.model.*
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.ELB
import com.equisoft.awsmocks.common.interfaces.http.accountId
import com.equisoft.awsmocks.common.interfaces.http.region
import java.util.Date
import kotlin.random.Random

private const val DEFAULT_HEALTH_CHECK_INTERVAL_SECONDS = 30
private const val DEFAULT_HEALTH_CHECK_TIMEOUT = 5
private const val DEFAULT_HEALTHY_THRESHOLD_COUNT = 5

fun createTargetGroupFromRequest(request: CreateTargetGroupRequest): TargetGroup {
    return TargetGroup()
        .withTargetGroupName(request.name)
        .withTargetGroupArn(ELB.createArn(request.accountId,
            "targetgroup/${request.name}/${randomString()}", request.region))
        .withHealthCheckEnabled(request.healthCheckEnabled ?: false)
        .withHealthCheckIntervalSeconds(request.healthCheckIntervalSeconds ?: DEFAULT_HEALTH_CHECK_INTERVAL_SECONDS)
        .withHealthCheckPath(request.healthCheckPath)
        .withHealthCheckPort(request.healthCheckPort ?: "traffic-port")
        .withHealthCheckProtocol(request.healthCheckProtocol ?: "HTTP")
        .withHealthCheckTimeoutSeconds(request.healthCheckTimeoutSeconds ?: DEFAULT_HEALTH_CHECK_TIMEOUT)
        .withHealthyThresholdCount(request.healthyThresholdCount ?: DEFAULT_HEALTHY_THRESHOLD_COUNT)
        .withMatcher(request.matcher)
        .withPort(request.port)
        .withProtocol(request.protocol)
        .withTargetType(request.targetType)
        .withUnhealthyThresholdCount(request.unhealthyThresholdCount)
        .withVpcId(request.vpcId)
}

fun createLoadBalancerFromRequest(request: CreateLoadBalancerRequest): LoadBalancer = LoadBalancer()
    .withAvailabilityZones(request.subnets.map { AvailabilityZone().withSubnetId(it).withZoneName("az") })
    .withCanonicalHostedZoneId(randomString())
    .withLoadBalancerArn(ELB.createArn(request.accountId,
        "loadbalancer/${getLoadBalancerShortType(
            request.type)}/${request.name}/${randomString()}", request.region))
    .withLoadBalancerName(request.name)
    .withCreatedTime(Date())
    .withDNSName(randomString())
    .withIpAddressType(request.ipAddressType)
    .withScheme(request.scheme)
    .withSecurityGroups(request.securityGroups)
    .withState(LoadBalancerState().withCode(LoadBalancerStateEnum.Active))
    .withType(request.type)

fun createListenerFromRequest(request: CreateListenerRequest): Listener = Listener()
    .withListenerArn("${request.loadBalancerArn.replace(":loadbalancer", ":listener")}/${randomString()}")
    .withCertificates(request.certificates)
    .withDefaultActions(request.defaultActions)
    .withLoadBalancerArn(request.loadBalancerArn)
    .withPort(request.port)
    .withProtocol(request.protocol)
    .withSslPolicy(request.sslPolicy)

fun createRuleFromRequest(request: CreateRuleRequest): Rule = Rule()
    .withRuleArn("${request.listenerArn.replace(":listener", ":listener-rule")}/${randomString()}")
    .withActions(request.actions)
    .withConditions(request.conditions)
    .withPriority(request.priority?.toString())

private fun getLoadBalancerShortType(type: String): String = if (type == "network") "network" else "app"

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
private fun randomString(length: Int = 16): String = (1..length)
    .map { Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")
