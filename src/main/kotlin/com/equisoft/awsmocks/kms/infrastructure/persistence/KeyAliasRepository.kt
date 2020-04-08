package com.equisoft.awsmocks.kms.infrastructure.persistence

import com.amazonaws.services.kms.model.AliasListEntry
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class KeyAliasRepository : ConcurrentMap<String, AliasListEntry> by ConcurrentHashMap() {
    @Synchronized
    fun getByKeyId(id: String): List<AliasListEntry> = values.filter { it.targetKeyId == id }

    @Synchronized
    fun getAll(): List<AliasListEntry> = values.toList()

    @Synchronized
    fun findByArn(aliasArn: String): AliasListEntry? = values.find { it.aliasArn == aliasArn }
}
