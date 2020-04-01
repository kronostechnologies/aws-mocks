package com.equisoft.awsmocks.elb.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.elasticloadbalancingv2.model.*
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.common.interfaces.http.toList
import com.equisoft.awsmocks.common.interfaces.http.toObjects
import com.equisoft.awsmocks.common.interfaces.http.toPairs
import io.ktor.http.Parameters

@SuppressWarnings("LongMethod")
class ElbParametersDeserializer : ParametersDeserializer {
    @OptIn(ExperimentalStdlibApi::class)
    override fun addParameters(parameters: Parameters, request: AmazonWebServiceRequest): AmazonWebServiceRequest =
        when (request) {
            is AddTagsRequest -> {
                request.withResourceArns(parameters.toList("ResourceArns.member"))
                    .withTags(parameters.getTags())
            }
            is CreateListenerRequest -> {
                request.withCertificates(parseCertificates(parameters))
                    .withDefaultActions(parseActions("DefaultActions", parameters))
                    .withLoadBalancerArn(parameters["LoadBalancerArn"])
                    .withPort(parameters["Port"]?.toInt())
                    .withProtocol(parameters["Protocol"])
                    .withSslPolicy(parameters["SslPolicy"])
            }
            is CreateLoadBalancerRequest -> {
                request.withIpAddressType(parameters["IpAddressType"])
                    .withName(parameters["Name"])
                    .withScheme(parameters["Scheme"])
                    .withSecurityGroups(parameters.toList("SecurityGroups.member"))
                    .withSubnets(parameters.toList("Subnets.member"))
                    .withTags(parameters.getTags())
                    .withType(parameters["Type"])
            }
            is CreateRuleRequest -> {
                request.withActions(parseActions("Actions", parameters))
                    .withConditions(parseConditions(parameters))
                    .withListenerArn(parameters["ListenerArn"])
                    .withPriority(parameters["Priority"]?.toInt())
            }
            is CreateTargetGroupRequest -> {
                request.withName(parameters["Name"])
                    .withHealthCheckEnabled(parameters["HealthCheckEnabled"]?.toBoolean())
                    .withHealthCheckIntervalSeconds(parameters["HealthCheckIntervalSeconds"]?.toInt())
                    .withHealthCheckPath(parameters["HealthCheckPath"])
                    .withHealthCheckPort(parameters["HealthCheckPort"])
                    .withHealthCheckProtocol(parameters["HealthCheckProtocol"])
                    .withHealthCheckTimeoutSeconds(parameters["HealthCheckTimeoutSeconds"]?.toInt())
                    .withHealthyThresholdCount(parameters["HealthyThresholdCount"]?.toInt())
                    .withMatcher(parameters["Matcher.HttpCode"]?.let { Matcher().withHttpCode(it) })
                    .withPort(parameters["Port"]?.toInt())
                    .withProtocol(parameters["Protocol"])
                    .withTargetType(parameters["TargetType"])
                    .withUnhealthyThresholdCount(parameters["UnhealthyThresholdCount"]?.toInt())
                    .withVpcId(parameters["VpcId"])
            }
            is DescribeListenersRequest -> {
                request.withListenerArns(parameters.toList("ListenerArns.member"))
                    .withLoadBalancerArn(parameters["LoadBalancerArn"])
            }
            is DescribeLoadBalancerAttributesRequest -> {
                request.withLoadBalancerArn(parameters["LoadBalancerArn"])
            }
            is DescribeLoadBalancersRequest -> {
                request.withLoadBalancerArns(parameters.toList("LoadBalancerArns.member"))
                    .withNames(parameters.toList("Names.member"))
            }
            is DescribeRulesRequest -> {
                request.withListenerArn(parameters["ListenerArn"])
                    .withRuleArns(parameters.toList("RuleArns.member"))
            }
            is DescribeTagsRequest -> {
                request.withResourceArns(parameters.toList("ResourceArns.member"))
            }
            is DescribeTargetGroupAttributesRequest -> {
                request.withTargetGroupArn(parameters["TargetGroupArn"])
            }
            is DescribeTargetGroupsRequest -> {
                request.withLoadBalancerArn(parameters["LoadBalancerArn"])
                    .withNames(parameters.toList("Names.member"))
                    .withTargetGroupArns(parameters.toList("TargetGroupArns.member"))
            }
            is ModifyLoadBalancerAttributesRequest -> {
                request.withAttributes(parseLoadBalancerAttributes(parameters))
                    .withLoadBalancerArn(parameters["LoadBalancerArn"])
            }
            is ModifyTargetGroupAttributesRequest -> {
                request.withAttributes(parseTargetGroupAttributes(parameters))
                    .withTargetGroupArn(parameters["TargetGroupArn"])
            }
            is ModifyTargetGroupRequest -> {
                request
                    .withHealthCheckEnabled(parameters["HealthCheckEnabled"]?.toBoolean())
                    .withHealthCheckIntervalSeconds(parameters["HealthCheckIntervalSeconds"]?.toInt())
                    .withHealthCheckPath(parameters["HealthCheckPath"])
                    .withHealthCheckPort(parameters["HealthCheckPort"])
                    .withHealthCheckProtocol(parameters["HealthCheckProtocol"])
                    .withHealthCheckTimeoutSeconds(parameters["HealthCheckTimeoutSeconds"]?.toInt())
                    .withHealthyThresholdCount(parameters["HealthyThresholdCount"]?.toInt())
                    .withMatcher(parameters["Matcher.HttpCode"]?.let { Matcher().withHttpCode(it) })
                    .withTargetGroupArn(parameters["TargetGroupArn"])
                    .withUnhealthyThresholdCount(parameters["UnhealthyThresholdCount"]?.toInt())
            }
            is SetSecurityGroupsRequest -> {
                request.withLoadBalancerArn(parameters["LoadBalancerArn"])
                    .withSecurityGroups(parameters.toList("SecurityGroups.member"))
            }
            else -> request
        }

    private fun parseCertificates(parameters: Parameters): List<Certificate> = parameters.toObjects(
        "Certificate.member", { Certificate().withIsDefault(true) }) { _, certificate, (key, value) ->
        when (key) {
            "CertificateArn" -> certificate.withCertificateArn(value)
            else -> certificate
        }
    }

    private fun parseConditions(parameters: Parameters): List<RuleCondition> = parameters.toObjects(
        "Conditions.member", { RuleCondition() }) { index, rule, (key, value) ->
        val toValuesList: (String) -> List<String> = { name ->
            parameters.toList("Conditions.member.$index.$name.Values.member")
        }

        when {
            key == "Field" -> rule.withField(value)
            key.startsWith("HostHeaderConfig") -> rule.withHostHeaderConfig(HostHeaderConditionConfig()
                .withValues(toValuesList("HostHeaderConfig")))
            key.startsWith("HttpHeaderConfig") -> rule.withHttpHeaderConfig(HttpHeaderConditionConfig()
                .withHttpHeaderName(parameters["Conditions.member.$index.HttpHeaderConfig.HttpHeaderName"])
                .withValues(toValuesList("HttpHeaderConfig")))
            key.startsWith("HttpRequestMethodConfig") -> rule.withHttpRequestMethodConfig(
                HttpRequestMethodConditionConfig().withValues(toValuesList("HttpRequestMethodConfig")))
            key.startsWith("PathPatternConfig") -> rule.withPathPatternConfig(PathPatternConditionConfig()
                .withValues(toValuesList("PathPatternConfig")))
            key.startsWith("QueryStringConfig") -> rule.withQueryStringConfig(QueryStringConditionConfig()
                .withValues(
                    parameters.toPairs("Conditions.member.$index.QueryStringConfig.Values.member", "Key", "Value")
                        .map { QueryStringKeyValuePair().withKey(it.first).withValue(it.second) }))
            key.startsWith("SourceIpConfig") -> rule.withSourceIpConfig(SourceIpConditionConfig()
                .withValues(toValuesList("SourceIpConfig")))
            key.startsWith("Values") -> rule
            else -> rule
        }
    }

    private fun parseActions(name: String, parameters: Parameters): List<Action> = parameters.toObjects(
        "$name.member", { Action() }) { index, action, (key, value) ->
        when {
            key.startsWith("AuthenticateCognitoConfig") -> action.withAuthenticateCognitoConfig(
                parseAuthenticateCognitoConfig("$name.member.$index.AuthenticateCognitoConfig", parameters)
            )
            key.startsWith("AuthenticateOidcConfig") -> action.withAuthenticateOidcConfig(
                parseAuthenticateOidcActionConfig("$name.member.$index.AuthenticateOidcConfig", parameters)
            )
            key.startsWith("FixedResponseConfig") -> action.withFixedResponseConfig(
                parseFixedResponseConfig("$name.member.$index.FixedResponseConfig", parameters)
            )
            key.startsWith("ForwardConfig") -> action.withForwardConfig(
                parseForwardConfig("$name.member.$index.ForwardConfig", parameters)
            )
            key == "Order" -> action.withOrder(value.toInt())
            key.startsWith("RedirectConfig") -> action.withRedirectConfig(
                parseRedirectConfig("$name.member.$index.RedirectConfig", parameters)
            )
            key == "TargetGroupArn" -> action.withTargetGroupArn(value)
            key == "Type" -> action.withType(value)
            else -> action
        }
    }

    private fun parseAuthenticateCognitoConfig(name: String, parameters: Parameters): AuthenticateCognitoActionConfig? {
        val userPoolArn: String? = parameters["$name.UserPoolArn"]
        return userPoolArn?.let {
            AuthenticateCognitoActionConfig().withAuthenticationRequestExtraParams(parameters.toPairs(
                "$name.AuthenticationRequestExtraParams.entry", "key", "value").toMap())
                .withOnUnauthenticatedRequest(parameters["$name.OnUnauthenticatedRequest"])
                .withScope(parameters["$name.Scope"])
                .withSessionCookieName(parameters["$name.SessionCookieName"])
                .withSessionTimeout(parameters["$name.SessionTimeout"]?.toLong())
                .withUserPoolArn(userPoolArn)
                .withUserPoolClientId(parameters["$name.UserPoolClientId"])
                .withUserPoolDomain(parameters["$name.UserPoolDomain"])
        }
    }

    private fun parseAuthenticateOidcActionConfig(name: String, parameters: Parameters): AuthenticateOidcActionConfig? {
        val authorizationEndpoint: String? = parameters["$name.AuthorizationEndpoint"]
        return authorizationEndpoint?.let {
            AuthenticateOidcActionConfig().withAuthenticationRequestExtraParams(parameters.toPairs(
                "$name.AuthenticationRequestExtraParams.entry", "key", "value").toMap())
                .withAuthorizationEndpoint(authorizationEndpoint)
                .withClientId(parameters["$name.ClientId"])
                .withClientSecret(parameters["$name.ClientSecret"])
                .withIssuer(parameters["$name.Issuer"])
                .withOnUnauthenticatedRequest(parameters["$name.OnUnauthenticatedRequest"])
                .withScope(parameters["$name.Scope"])
                .withSessionCookieName(parameters["$name.SessionCookieName"])
                .withSessionTimeout(parameters["$name.SessionTimeout"]?.toLong())
                .withTokenEndpoint(parameters["$name.TokenEndpoint"])
                .withUseExistingClientSecret(parameters["$name.UseExistingClientSecret"]?.toBoolean())
                .withUserInfoEndpoint(parameters["$name.UserInfoEndpoint"])
        }
    }

    private fun parseFixedResponseConfig(name: String, parameters: Parameters): FixedResponseActionConfig? {
        val statusCode: String? = parameters["$name.StatusCode"]
        return statusCode?.let {
            FixedResponseActionConfig().withContentType(parameters["$name.ContentType"])
                .withMessageBody(parameters["$name.MessageBody"])
                .withStatusCode(statusCode)
        }
    }

    private fun parseForwardConfig(name: String, parameters: Parameters): ForwardActionConfig? {
        val targetGroups: List<TargetGroupTuple> =
            parameters.toPairs("$name.TargetGroups.member", "TargetGroupArn", "Weight")
                .map { TargetGroupTuple().withTargetGroupArn(it.first).withWeight(it.second.toInt()) }

        val stickinessConfig = TargetGroupStickinessConfig()
            .withDurationSeconds(parameters["$name.TargetGroupStickinessConfig.DurationSeconds"]?.toInt())
            .withEnabled(parameters["$name.TargetGroupStickinessConfig.Enabled"]?.toBoolean())

        return if (targetGroups.isNotEmpty() ||
            stickinessConfig.durationSeconds != null ||
            stickinessConfig.enabled != null
        ) {
            ForwardActionConfig()
                .withTargetGroupStickinessConfig(null)
                .withTargetGroups(targetGroups)
        } else null
    }

    private fun parseRedirectConfig(name: String, parameters: Parameters): RedirectActionConfig? {
        val statusCode: String? = parameters["$name.StatusCode"]
        return statusCode?.let {
            RedirectActionConfig().withHost(parameters["$name.Host"])
                .withPath(parameters["$name.Path"])
                .withPort(parameters["$name.Port"])
                .withProtocol(parameters["$name.Protocol"])
                .withQuery(parameters["$name.Query"])
                .withStatusCode(statusCode)
        }
    }

    private fun parseLoadBalancerAttributes(parameters: Parameters): List<LoadBalancerAttribute> =
        parameters.toObjects("Attributes.member", { LoadBalancerAttribute() }) { _, attribute, (key, value) ->
            when (key) {
                "Key" -> attribute.withKey(key)
                "Value" -> attribute.withValue(value)
                else -> attribute
            }
        }

    private fun parseTargetGroupAttributes(parameters: Parameters): List<TargetGroupAttribute> =
        parameters.toObjects("Attributes.member", { TargetGroupAttribute() }) { _, attribute, (key, value) ->
            when (key) {
                "Key" -> attribute.withKey(key)
                "Value" -> attribute.withValue(value)
                else -> attribute
            }
        }
}

private fun Parameters.getTags(name: String = "Tags.member"): List<Tag> = toObjects(name, { Tag() },
    { _, tag, (key, value) ->
        when (key) {
            "Key" -> tag.withKey(value)
            "Value" -> tag.withValue(value)
            else -> tag
        }
    })
