package com.equisoft.awsmocks.route53.infrastructure.persistence

import com.amazonaws.services.route53.model.HostedZone
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class HostedZoneRepository private constructor(
    private val delegate: ConcurrentHashMap<String, HostedZone>
) : ConcurrentMap<String, HostedZone> by delegate {
    constructor() : this(ConcurrentHashMap())

    @Synchronized
    override fun get(key: String?): HostedZone? = key?.let { delegate[key.removeSuffix(".")] }
}
