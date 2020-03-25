package com.equisoft.awsmocks.kms.application

import com.amazonaws.services.kms.model.AliasListEntry
import com.amazonaws.services.kms.model.KeyMetadata
import com.amazonaws.services.kms.model.Tag
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.kms.infrastructure.persistence.KeyAliasRepository
import com.equisoft.awsmocks.kms.infrastructure.persistence.KeyMetadataRepository

class KeyService(
    private val keyMetadataRepository: KeyMetadataRepository,
    private val keyAliasRepository: KeyAliasRepository,
    private val tagsRepository: ResourceTagsRepository<Tag>
) {
    fun addOrUpdate(key: KeyMetadata) {
        keyMetadataRepository[key.keyId] = key
    }

    fun addTags(id: String, tags: List<Tag>) {
        val key: KeyMetadata = get(id)
        tagsRepository.update(key.keyId, tags, listOf())
    }

    fun get(id: String): KeyMetadata =
        keyMetadataRepository[id] ?: throw NotFoundException()

    fun updateDescription(id: String, description: String) {
        val key: KeyMetadata = get(id).withDescription(description)
        keyMetadataRepository[id] = key
    }

    fun getTags(id: String): List<Tag> {
        val key: KeyMetadata = get(id)
        return tagsRepository.getOrDefault(key.keyId, listOf())
    }

    fun addOrUpdateAlias(id: String, aliasName: String) {
        val key: KeyMetadata = get(id)
        keyAliasRepository[aliasName] = AliasListEntry()
            .withAliasName(aliasName)
            .withTargetKeyId(key.keyId)
            .withAliasArn(key.arn.substringBeforeLast(":") + ":$aliasName")
    }

    fun removeAlias(aliasName: String) {
        keyAliasRepository.remove(aliasName)
    }

    fun getAliases(id: String?): List<AliasListEntry> {
        if (id == null) {
            return keyAliasRepository.getAll()
        }

        val key: KeyMetadata = get(id)
        return keyAliasRepository.getByKeyId(key.keyId)
    }
}
