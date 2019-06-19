package com.equisoft.awsmocks.acm.application

import com.amazonaws.services.certificatemanager.model.CertificateDetail
import com.amazonaws.services.certificatemanager.model.Tag
import com.equisoft.awsmocks.acm.infrastructure.persistence.CertificateRepository
import com.equisoft.awsmocks.common.exceptions.ResourceNotFoundException
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository

class CertificateService(
    private val certificateRepository: CertificateRepository,
    private val tagsRepository: ResourceTagsRepository<Tag>
) {
    fun addOrUpdate(certificate: CertificateDetail) {
        certificateRepository[certificate.certificateArn] = certificate
    }

    fun addTags(arn: String, tags: List<Tag>) {
        val certificate: CertificateDetail = checkNotNull(certificateRepository[arn])
        tagsRepository.update(certificate.certificateArn, tags, listOf())
    }

    fun get(certificateArn: String): CertificateDetail =
        certificateRepository[certificateArn] ?: throw ResourceNotFoundException()

    fun getTags(arn: String): List<Tag> {
        val certificate: CertificateDetail = checkNotNull(certificateRepository[arn])
        return tagsRepository.getOrDefault(certificate.certificateArn, listOf())
    }
}
