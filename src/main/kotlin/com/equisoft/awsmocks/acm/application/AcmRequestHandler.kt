package com.equisoft.awsmocks.acm.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.certificatemanager.model.AddTagsToCertificateRequest
import com.amazonaws.services.certificatemanager.model.AddTagsToCertificateResult
import com.amazonaws.services.certificatemanager.model.CertificateDetail
import com.amazonaws.services.certificatemanager.model.DescribeCertificateRequest
import com.amazonaws.services.certificatemanager.model.DescribeCertificateResult
import com.amazonaws.services.certificatemanager.model.ListTagsForCertificateRequest
import com.amazonaws.services.certificatemanager.model.ListTagsForCertificateResult
import com.amazonaws.services.certificatemanager.model.RequestCertificateRequest
import com.amazonaws.services.certificatemanager.model.RequestCertificateResult
import com.amazonaws.services.certificatemanager.model.Tag

@SuppressWarnings("LongMethod")
class AcmRequestHandler(private val certificateService: CertificateService) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> =
        when (request) {
            is AddTagsToCertificateRequest -> {
                certificateService.addTags(request.certificateArn, request.tags)

                AddTagsToCertificateResult()
            }
            is DescribeCertificateRequest -> {
                val certificateDetail: CertificateDetail = certificateService.get(request.certificateArn)

                DescribeCertificateResult().withCertificate(certificateDetail)
            }
            is ListTagsForCertificateRequest -> {
                val tags: List<Tag> = certificateService.getTags(request.certificateArn)

                ListTagsForCertificateResult().withTags(tags)
            }
            is RequestCertificateRequest -> {
                val certificate: CertificateDetail = certificateDetailFromRequest(request)
                certificateService.addOrUpdate(certificate)

                RequestCertificateResult().withCertificateArn(certificate.certificateArn)
            }
            else -> throw IllegalArgumentException(request::class.simpleName)
        }
}
