package com.equisoft.awsmocks.common.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.ApplicationCall
import io.ktor.application.log
import io.ktor.request.receiveText

private const val TARGET = "X-Amz-Target"

class JsonRequestFactory(
    private val objectMapper: ObjectMapper,
    private val rootPackage: String
) {
    suspend fun create(call: ApplicationCall): AmazonWebServiceRequest {
        val content: String = call.receiveText().also { logRequest(call, it) }
        val target: String = checkNotNull(call.request.headers[TARGET])
        val actionName: String = target.replaceBefore('.', "").drop(1)
        val klass: Class<*> = Class.forName("$rootPackage.${actionName}Request")

        val request: AmazonWebServiceRequest = objectMapper.readValue(content, klass) as AmazonWebServiceRequest
        request.parseAuthorization(call.request)

        return request
    }

    private fun logRequest(call: ApplicationCall, content: String) {
        call.application.log.debug(content)
    }
}
