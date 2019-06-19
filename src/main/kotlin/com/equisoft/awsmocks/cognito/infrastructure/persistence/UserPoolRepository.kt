package com.equisoft.awsmocks.cognito.infrastructure.persistence

import com.amazonaws.services.cognitoidp.model.UserPoolType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class UserPoolRepository : ConcurrentMap<String, UserPoolType> by ConcurrentHashMap()
