package com.equisoft.awsmocks.kms.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.kms.model.AliasListEntry
import com.amazonaws.services.kms.model.CreateAliasRequest
import com.amazonaws.services.kms.model.CreateAliasResult
import com.amazonaws.services.kms.model.CreateKeyRequest
import com.amazonaws.services.kms.model.CreateKeyResult
import com.amazonaws.services.kms.model.DeleteAliasRequest
import com.amazonaws.services.kms.model.DeleteAliasResult
import com.amazonaws.services.kms.model.DescribeKeyRequest
import com.amazonaws.services.kms.model.DescribeKeyResult
import com.amazonaws.services.kms.model.GetKeyPolicyRequest
import com.amazonaws.services.kms.model.GetKeyPolicyResult
import com.amazonaws.services.kms.model.GetKeyRotationStatusRequest
import com.amazonaws.services.kms.model.GetKeyRotationStatusResult
import com.amazonaws.services.kms.model.KeyMetadata
import com.amazonaws.services.kms.model.ListAliasesRequest
import com.amazonaws.services.kms.model.ListAliasesResult
import com.amazonaws.services.kms.model.ListResourceTagsRequest
import com.amazonaws.services.kms.model.ListResourceTagsResult
import com.amazonaws.services.kms.model.Tag
import com.amazonaws.services.kms.model.TagResourceRequest
import com.amazonaws.services.kms.model.TagResourceResult
import com.amazonaws.services.kms.model.UpdateKeyDescriptionRequest
import com.amazonaws.services.kms.model.UpdateKeyDescriptionResult

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
