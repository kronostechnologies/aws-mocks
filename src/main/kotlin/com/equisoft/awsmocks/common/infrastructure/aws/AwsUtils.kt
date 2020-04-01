@file:SuppressWarnings("MatchingDeclarationName")

package com.equisoft.awsmocks.common.infrastructure.aws

enum class AwsService(private val serviceName: String) {
    ACM("acm"),
    COGNITO("cognito"),
    EC2("ec2"),
    ECS("ecs"),
    KMS("kms"),
    IAM("iam"),
    ;

    fun createArn(accountId: String?, resource: String, region: String? = null): String =
        "arn:aws:$serviceName:${region ?: "us-east-1"}:${accountId ?: ""}:$resource"
}

fun isArn(value: String) = value.startsWith("arn:")
