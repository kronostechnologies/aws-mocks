package com.equisoft.awsmocks.kms.infrastructure.persistence

import com.amazonaws.services.kms.model.AliasListEntry
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class KeyAliasRepository : ConcurrentMap<String, AliasListEntry> by ConcurrentHashMap() {
    fun getByKeyId(id: String): List<AliasListEntry> = values.filter { it.targetKeyId == id }

    fun getAll(): List<AliasListEntry> = values.toList()
}