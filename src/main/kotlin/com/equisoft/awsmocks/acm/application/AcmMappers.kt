@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.acm.application

import com.amazonaws.services.certificatemanager.model.CertificateDetail
import com.amazonaws.services.certificatemanager.model.CertificateStatus
import com.amazonaws.services.certificatemanager.model.CertificateType
import com.amazonaws.services.certificatemanager.model.DomainStatus
import com.amazonaws.services.certificatemanager.model.DomainValidation
import com.amazonaws.services.certificatemanager.model.RecordType
import com.amazonaws.services.certificatemanager.model.RequestCertificateRequest
import com.amazonaws.services.certificatemanager.model.ResourceRecord
import com.equisoft.awsmocks.common.interfaces.http.accountId
import java.util.Date
import java.util.UUID

fun certificateDetailFromRequest(request: RequestCertificateRequest): CertificateDetail {
    val certificateId: String = UUID.randomUUID().toString()

    return CertificateDetail()
        .withCertificateArn("arn:aws:acm:us-east-1:${request.accountId}:certificate/$certificateId")
        .withSerial(certificateId)
        .withDomainName(request.domainName)
        .withStatus(CertificateStatus.ISSUED)
        .withType(CertificateType.AMAZON_ISSUED)
        .withIssuedAt(Date())
        .withDomainValidationOptions(
            DomainValidation()
                .withValidationMethod(request.validationMethod)
                .withDomainName(request.domainName)
                .withValidationStatus(DomainStatus.SUCCESS)
                .withResourceRecord(
                    ResourceRecord()
                        .withType(RecordType.CNAME)
                        .withName("$certificateId.${request.domainName}")
                        .withValue("$certificateId.${request.domainName}"))
        )
}
