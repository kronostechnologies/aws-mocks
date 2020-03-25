package com.equisoft.awsmocks.cognito.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.cognitoidp.model.CreateResourceServerRequest
import com.amazonaws.services.cognitoidp.model.CreateResourceServerResult
import com.amazonaws.services.cognitoidp.model.CreateUserPoolClientRequest
import com.amazonaws.services.cognitoidp.model.CreateUserPoolClientResult
import com.amazonaws.services.cognitoidp.model.CreateUserPoolDomainRequest
import com.amazonaws.services.cognitoidp.model.CreateUserPoolDomainResult
import com.amazonaws.services.cognitoidp.model.CreateUserPoolRequest
import com.amazonaws.services.cognitoidp.model.CreateUserPoolResult
import com.amazonaws.services.cognitoidp.model.DeleteUserPoolRequest
import com.amazonaws.services.cognitoidp.model.DeleteUserPoolResult
import com.amazonaws.services.cognitoidp.model.DescribeResourceServerRequest
import com.amazonaws.services.cognitoidp.model.DescribeResourceServerResult
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolClientRequest
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolClientResult
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolDomainRequest
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolDomainResult
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolRequest
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolResult
import com.amazonaws.services.cognitoidp.model.DomainDescriptionType
import com.amazonaws.services.cognitoidp.model.GetUserPoolMfaConfigRequest
import com.amazonaws.services.cognitoidp.model.GetUserPoolMfaConfigResult
import com.amazonaws.services.cognitoidp.model.ResourceServerType
import com.amazonaws.services.cognitoidp.model.SetUserPoolMfaConfigRequest
import com.amazonaws.services.cognitoidp.model.SetUserPoolMfaConfigResult
import com.amazonaws.services.cognitoidp.model.SmsMfaConfigType
import com.amazonaws.services.cognitoidp.model.UpdateUserPoolClientRequest
import com.amazonaws.services.cognitoidp.model.UpdateUserPoolClientResult
import com.amazonaws.services.cognitoidp.model.UpdateUserPoolRequest
import com.amazonaws.services.cognitoidp.model.UserPoolClientType
import com.amazonaws.services.cognitoidp.model.UserPoolType

@SuppressWarnings("LongMethod")
class CognitoRequestHandler(private val userPoolService: UserPoolService) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> =
        when (request) {
            is CreateResourceServerRequest -> {
                val resourceServer: ResourceServerType = resourceServerTypeFromRequest(request)
                userPoolService.addResourceServer(request.userPoolId, resourceServer)

                CreateResourceServerResult()
            }
            is CreateUserPoolClientRequest -> {
                val userPoolClient: UserPoolClientType = userPoolClientTypeFromRequest(request)
                userPoolService.addOrUpdateAppClient(request.userPoolId, userPoolClient)

                CreateUserPoolClientResult().withUserPoolClient(userPoolClient)
            }
            is CreateUserPoolDomainRequest -> {
                val domain: DomainDescriptionType = domainDescriptionTypeFromRequest(request)
                userPoolService.addDomain(request.userPoolId, domain)

                CreateUserPoolDomainResult().withCloudFrontDomain(request.domain)
            }
            is CreateUserPoolRequest -> {
                val userPool: UserPoolType = userPoolTypeFromRequest(request)
                userPoolService.addOrUpdate(userPool)

                CreateUserPoolResult().withUserPool(userPool)
            }
            is DeleteUserPoolRequest -> {
                userPoolService.delete(request.userPoolId)

                DeleteUserPoolResult()
            }
            is DescribeResourceServerRequest -> {
                val resourceServer: ResourceServerType =
                    userPoolService.getResourceServer(request.userPoolId, request.identifier)

                DescribeResourceServerResult().withResourceServer(resourceServer)
            }
            is DescribeUserPoolClientRequest -> {
                val userPoolClient: UserPoolClientType = userPoolService.getClient(request.userPoolId, request.clientId)

                DescribeUserPoolClientResult().withUserPoolClient(userPoolClient)
            }
            is DescribeUserPoolDomainRequest -> {
                val domain: DomainDescriptionType = userPoolService.getDomain(request.domain)
                DescribeUserPoolDomainResult().withDomainDescription(domain)
            }
            is DescribeUserPoolRequest -> {
                val userPool: UserPoolType = userPoolService.get(request.userPoolId)

                DescribeUserPoolResult().withUserPool(userPool)
            }
            is GetUserPoolMfaConfigRequest -> {
                val userPool: UserPoolType = userPoolService.get(request.userPoolId)

                GetUserPoolMfaConfigResult()
                    .withMfaConfiguration(userPool.mfaConfiguration)
                    .withSmsMfaConfiguration(SmsMfaConfigType()
                        .withSmsAuthenticationMessage(userPool.smsAuthenticationMessage)
                        .withSmsConfiguration(userPool.smsConfiguration)
                    )
            }
            is SetUserPoolMfaConfigRequest -> {
                userPoolService.setMfaConfig(
                    request.userPoolId,
                    request.mfaConfiguration,
                    request.smsMfaConfiguration
                )

                SetUserPoolMfaConfigResult()
                    .withMfaConfiguration(request.mfaConfiguration)
                    .withSmsMfaConfiguration(request.smsMfaConfiguration)
                    .withSoftwareTokenMfaConfiguration(request.softwareTokenMfaConfiguration)
            }
            is UpdateUserPoolClientRequest -> {
                val userPoolClient: UserPoolClientType = updateUserPoolClientFromRequest(request)
                userPoolService.addOrUpdateAppClient(request.userPoolId, userPoolClient)

                UpdateUserPoolClientResult().withUserPoolClient(userPoolClient)
            }
            is UpdateUserPoolRequest -> {
                val userPool: UserPoolType = userPoolService.get(request.userPoolId)
                val updatedUserPool: UserPoolType = updateUserPoolTypeFromRequest(request, userPool)
                userPoolService.addOrUpdate(updatedUserPool)

                CreateUserPoolResult().withUserPool(updatedUserPool)
            }
            else -> throw IllegalArgumentException(request::class.simpleName)
        }
}
