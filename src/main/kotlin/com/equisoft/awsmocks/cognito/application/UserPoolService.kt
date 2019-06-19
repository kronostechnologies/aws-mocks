package com.equisoft.awsmocks.cognito.application

import com.amazonaws.services.cognitoidp.model.DomainDescriptionType
import com.amazonaws.services.cognitoidp.model.ResourceServerType
import com.amazonaws.services.cognitoidp.model.UserPoolClientType
import com.amazonaws.services.cognitoidp.model.UserPoolType
import com.equisoft.awsmocks.cognito.infrastructure.persistence.ResourceServerRepository
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolClientRepository
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolDomainRepository
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolRepository
import com.equisoft.awsmocks.common.exceptions.ResourceNotFoundException

class UserPoolService(
    private val userPoolRepository: UserPoolRepository,
    private val userPoolClientRepository: UserPoolClientRepository,
    private val userPoolDomainRepository: UserPoolDomainRepository,
    private val resourceServerRepository: ResourceServerRepository
) {
    fun addOrUpdate(userPool: UserPoolType) {
        userPoolRepository[userPool.id] = userPool
    }

    fun get(userPoolId: String): UserPoolType =
        userPoolRepository[userPoolId] ?: throw ResourceNotFoundException()

    fun addOrUpdateAppClient(userPoolId: String, client: UserPoolClientType) {
        val userPool: UserPoolType = get(userPoolId)
        userPoolClientRepository.add(userPool.id, client)
    }

    fun getClient(userPoolId: String, clientId: String): UserPoolClientType {
        val userPool: UserPoolType = get(userPoolId)
        return userPoolClientRepository.find(userPool.id, clientId) ?: throw ResourceNotFoundException()
    }

    fun addDomain(userPoolId: String, domain: DomainDescriptionType) {
        val userPool: UserPoolType = get(userPoolId)
        userPoolDomainRepository.add(userPool.id, domain)
    }

    fun addResourceServer(userPoolId: String, resourceServer: ResourceServerType) {
        val userPool: UserPoolType = get(userPoolId)
        resourceServerRepository.add(userPool.id, resourceServer)
    }

    fun getResourceServer(userPoolId: String, resourceServerId: String): ResourceServerType {
        val userPool: UserPoolType = get(userPoolId)
        return resourceServerRepository.find(userPool.id, resourceServerId) ?: throw ResourceNotFoundException()
    }

    fun getDomain(domain: String): DomainDescriptionType = userPoolDomainRepository.find(domain)
        ?: throw ResourceNotFoundException()

    fun delete(userPoolId: String): UserPoolType? = userPoolRepository.remove(userPoolId)
}
