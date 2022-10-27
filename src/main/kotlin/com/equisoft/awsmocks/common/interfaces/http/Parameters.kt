@file:SuppressWarnings("TopLevelPropertyNaming")

package com.equisoft.awsmocks.common.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.equisoft.awsmocks.common.context.AwsEnvironment
import com.equisoft.awsmocks.common.context.applicationConfig
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.ApplicationRequest
import io.ktor.util.pipeline.PipelineContext

private const val AwsRegion = "AwsRegion"
private const val MIN_EXPECTED_PARAMETERS = 5

fun PipelineContext<Unit, ApplicationCall>.getIdParameter(): String = call.parameters["id"]!!

fun AmazonWebServiceRequest.parseAuthorization(request: ApplicationRequest) {
    val values: List<String>? = request.headers[HttpHeaders.Authorization]
        ?.substringAfter("Credential=")
        ?.substringBefore(",")
        ?.split("/")

    if (values != null) {
        if (values.size < MIN_EXPECTED_PARAMETERS) {
            throw IllegalArgumentException("Unexpected Credentials length")
        }

        region = values[2]
    }
}

val AmazonWebServiceRequest.accountId: String
    get() = applicationConfig[AwsEnvironment.accountId]

var AmazonWebServiceRequest.region: String?
    get() = customRequestHeaders[AwsRegion]
    set(value) {
        putCustomRequestHeader(AwsRegion, value)
    }
