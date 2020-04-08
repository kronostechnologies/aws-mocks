package com.equisoft.awsmocks.kms.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.kms.model.*

@SuppressWarnings("LongMethod")
class KmsRequestHandler(private val keyService: KeyService) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> {
        return when (request) {
            is CreateAliasRequest -> {
                keyService.addOrUpdateAlias(request.targetKeyId, request.aliasName)

                CreateAliasResult()
            }
            is CreateKeyRequest -> {
                val key: KeyMetadata = keyMetadataFromRequest(request)
                keyService.addOrUpdate(key)

                CreateKeyResult().withKeyMetadata(key)
            }
            is DeleteAliasRequest -> {
                keyService.removeAlias(request.aliasName)

                DeleteAliasResult()
            }
            is DescribeKeyRequest -> {
                val key: KeyMetadata = keyService.get(request.keyId)

                DescribeKeyResult().withKeyMetadata(key)
            }
            is EncryptRequest -> {
                val cipherText = keyService.encryptText(request.keyId, request.plaintext.asReadOnlyBuffer())

                EncryptResult().withCiphertextBlob(cipherText)
            }
            is GetKeyPolicyRequest -> {
                GetKeyPolicyResult().withPolicy(defaultPolicy(request))
            }
            is GetKeyRotationStatusRequest -> {
                GetKeyRotationStatusResult().withKeyRotationEnabled(false)
            }
            is ListAliasesRequest -> {
                val aliases: List<AliasListEntry> = keyService.getAliases(request.keyId)

                ListAliasesResult().withAliases(aliases).withTruncated(false)
            }
            is ListResourceTagsRequest -> {
                val tags: List<Tag> = keyService.getTags(request.keyId)

                ListResourceTagsResult().withTags(tags).withTruncated(false)
            }
            is UpdateKeyDescriptionRequest -> {
                keyService.updateDescription(request.keyId, request.description)

                UpdateKeyDescriptionResult()
            }
            is TagResourceRequest -> {
                keyService.addTags(request.keyId, request.tags)

                TagResourceResult()
            }
            else -> throw IllegalArgumentException(request::class.simpleName)
        }
    }
}
