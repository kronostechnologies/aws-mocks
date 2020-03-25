package com.equisoft.awsmocks.route53.infrastructure.persistence

import com.amazonaws.services.route53.model.DelegationSet
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DelegationSetRepository : ConcurrentMap<String, DelegationSet> by ConcurrentHashMap()
