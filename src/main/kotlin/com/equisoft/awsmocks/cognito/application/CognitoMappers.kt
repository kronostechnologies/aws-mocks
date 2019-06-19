@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.cognito.application

import com.amazonaws.services.cognitoidp.model.CreateResourceServerRequest
import com.amazonaws.services.cognitoidp.model.CreateUserPoolClientRequest
import com.amazonaws.services.cognitoidp.model.CreateUserPoolDomainRequest
import com.amazonaws.services.cognitoidp.model.CreateUserPoolRequest
import com.amazonaws.services.cognitoidp.model.DomainDescriptionType
import com.amazonaws.services.cognitoidp.model.DomainStatusType
import com.amazonaws.services.cognitoidp.model.PasswordPolicyType
import com.amazonaws.services.cognitoidp.model.ResourceServerType
import com.amazonaws.services.cognitoidp.model.StatusType
import com.amazonaws.services.cognitoidp.model.UpdateUserPoolClientRequest
import com.amazonaws.services.cognitoidp.model.UpdateUserPoolRequest
import com.amazonaws.services.cognitoidp.model.UserPoolClientType
import com.amazonaws.services.cognitoidp.model.UserPoolPolicyType
import com.amazonaws.services.cognitoidp.model.UserPoolType
import com.equisoft.awsmocks.common.interfaces.http.accountId
import java.util.Base64
import java.util.Date

fun domainDescriptionTypeFromRequest(request: CreateUserPoolDomainRequest): DomainDescriptionType =
    DomainDescriptionType()
        .withDomain(request.domain)
        .withAWSAccountId(request.accountId)
        .withCloudFrontDistribution("${base64Encode(request.domain)}.cloudfront.net")
        .withCustomDomainConfig(request.customDomainConfig)
        .withUserPoolId(request.userPoolId)
        .withStatus(DomainStatusType.ACTIVE)

fun resourceServerTypeFromRequest(request: CreateResourceServerRequest): ResourceServerType = ResourceServerType()
    .withIdentifier(request.identifier)
    .withName(request.name)
    .withScopes(request.scopes)
    .withUserPoolId(request.userPoolId)

fun userPoolClientTypeFromRequest(request: CreateUserPoolClientRequest): UserPoolClientType = UserPoolClientType()
    .withUserPoolId(request.userPoolId)
    .withClientName(request.clientName)
    .withAllowedOAuthFlows(request.allowedOAuthFlows)
    .withAllowedOAuthScopes(request.allowedOAuthScopes)
    .withAllowedOAuthFlowsUserPoolClient(request.allowedOAuthFlowsUserPoolClient)
    .withCreationDate(Date())
    .withLastModifiedDate(Date())
    .withRefreshTokenValidity(request.refreshTokenValidity)
    .withExplicitAuthFlows(request.explicitAuthFlows)
    .withReadAttributes(request.readAttributes)
    .withWriteAttributes(request.writeAttributes)
    .withClientId(base64Encode(request.clientName))
    .apply {
        withClientSecret(base64Encode(clientId))
    }

fun updateUserPoolClientFromRequest(request: UpdateUserPoolClientRequest): UserPoolClientType = UserPoolClientType()
    .withUserPoolId(request.userPoolId)
    .withClientName(request.clientName)
    .withAllowedOAuthFlows(request.allowedOAuthFlows)
    .withAllowedOAuthScopes(request.allowedOAuthScopes)
    .withAllowedOAuthFlowsUserPoolClient(request.allowedOAuthFlowsUserPoolClient)
    .withCreationDate(Date())
    .withLastModifiedDate(Date())
    .withRefreshTokenValidity(request.refreshTokenValidity)
    .withExplicitAuthFlows(request.explicitAuthFlows)
    .withReadAttributes(request.readAttributes)
    .withWriteAttributes(request.writeAttributes)
    .withClientId(base64Encode(request.clientName))
    .apply {
        withClientSecret(base64Encode(clientId))
    }

fun userPoolTypeFromRequest(request: CreateUserPoolRequest): UserPoolType = UserPoolType()
    .withId(request.poolName)
    .withCreationDate(Date())
    .withLastModifiedDate(Date())
    .withName(request.poolName)
    .withMfaConfiguration(request.mfaConfiguration)
    .withPolicies(request.policies ?: defaultPasswordPolicy())
    .withAdminCreateUserConfig(request.adminCreateUserConfig)
    .withAutoVerifiedAttributes(request.autoVerifiedAttributes)
    .withStatus(StatusType.Enabled)
    .withUserPoolTags(request.userPoolTags)
    .withUserPoolAddOns(request.userPoolAddOns)
    .withEmailConfiguration(request.emailConfiguration)
    .withEmailVerificationMessage(request.emailVerificationMessage)
    .withEmailVerificationSubject(request.emailVerificationSubject)
    .withSmsConfiguration(request.smsConfiguration)
    .withSmsVerificationMessage(request.smsVerificationMessage)
    .withSmsAuthenticationMessage(request.smsAuthenticationMessage)

fun updateUserPoolTypeFromRequest(
    request: UpdateUserPoolRequest,
    oldUserPool: UserPoolType
): UserPoolType = UserPoolType()
    .withId(oldUserPool.id)
    .withCreationDate(Date())
    .withLastModifiedDate(Date())
    .withName(oldUserPool.name)
    .withMfaConfiguration(request.mfaConfiguration)
    .withPolicies(request.policies ?: defaultPasswordPolicy())
    .withAdminCreateUserConfig(request.adminCreateUserConfig)
    .withAutoVerifiedAttributes(request.autoVerifiedAttributes)
    .withStatus(StatusType.Enabled)
    .withUserPoolTags(request.userPoolTags)
    .withUserPoolAddOns(request.userPoolAddOns)
    .withEmailConfiguration(request.emailConfiguration)
    .withEmailVerificationMessage(request.emailVerificationMessage)
    .withEmailVerificationSubject(request.emailVerificationSubject)
    .withSmsConfiguration(request.smsConfiguration)
    .withSmsVerificationMessage(request.smsVerificationMessage)
    .withSmsAuthenticationMessage(request.smsAuthenticationMessage)

@SuppressWarnings("MagicNumber")
private fun defaultPasswordPolicy(): UserPoolPolicyType? {
    return UserPoolPolicyType().withPasswordPolicy(
        PasswordPolicyType()
            .withMinimumLength(8)
            .withRequireLowercase(true)
            .withRequireNumbers(true)
            .withRequireSymbols(true)
            .withRequireUppercase(true)
    )
}

private fun base64Encode(value: String) =
    Base64.getEncoder().encodeToString(value.toByteArray())
