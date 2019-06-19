@file:SuppressWarnings("TopLevelPropertyNaming")

package com.equisoft.awsmocks.common.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.util.pipeline.PipelineContext

const val AwsAccountId = "AwsAccountId"

fun PipelineContext<Unit, ApplicationCall>.getIdParameter(): String = call.parameters["id"]!!

fun ApplicationCall.getAccountId(): String? = request.headers[HttpHeaders.Authorization]
    ?.substringAfter("Credential=")
    ?.substringBefore("/")

var AmazonWebServiceRequest.accountId: String?
    get() = customRequestHeaders[AwsAccountId]
    set(value) {
        putCustomRequestHeader(AwsAccountId, value)
    }
