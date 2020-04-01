package com.equisoft.awsmocks.cognito.infrastructure.persistence

import com.amazonaws.services.cognitoidp.model.UserPoolClientType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class UserPoolClientRepository : ConcurrentMap<String, List<UserPoolClientType>> by ConcurrentHashMap() {
    @Synchronized
    fun add(key: String, value: UserPoolClientType) {
        val clients: List<UserPoolClientType> = getOrPut(key, { listOf() })
            .filter { it.clientId != value.clientId }
        put(key, clients + value)
    }

    @Synchronized
    fun find(userPoolId: String, clientId: String): UserPoolClientType? =
        get(userPoolId)?.find { it.clientId == clientId }
}
