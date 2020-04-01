@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.kms.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.kms.model.CreateKeyRequest
import com.amazonaws.services.kms.model.KeyManagerType
import com.amazonaws.services.kms.model.KeyMetadata
import com.amazonaws.services.kms.model.KeyState
import com.amazonaws.services.kms.model.KeyUsageType
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.IAM
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.KMS
import com.equisoft.awsmocks.common.interfaces.http.accountId
import java.util.Date
import java.util.UUID

fun keyMetadataFromRequest(request: CreateKeyRequest): KeyMetadata = KeyMetadata()
    .withKeyId(UUID.randomUUID().toString())
    .withCustomerMasterKeySpec(request.customerMasterKeySpec)
    .withKeyManager(KeyManagerType.AWS)
    .withOrigin(request.origin)
    .withKeyState(KeyState.Enabled)
    .withKeyUsage(KeyUsageType.ENCRYPT_DECRYPT)
    .withAWSAccountId(request.accountId)
    .withCreationDate(Date())
    .withEnabled(true)
    .withDescription(request.description)
    .apply {
        withArn(KMS.createArn(request.accountId, "key/$keyId"))
    }

fun defaultPolicy(request: AmazonWebServiceRequest) = """{
    "Id":"key-default-1",
    "Statement":[{
        "Action":"kms:*",
        "Effect":"Allow",
        "Principal":{"AWS":"${IAM.createArn(request.accountId, "root", "")}"},
        "Resource":"*",
        "Sid":"Enable IAM User Permissions"
    }],
    "Version":"2012-10-17"
}
""".trimIndent()
