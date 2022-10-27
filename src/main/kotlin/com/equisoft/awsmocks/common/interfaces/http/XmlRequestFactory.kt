package com.equisoft.awsmocks.common.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.log
import io.ktor.server.request.receiveText

class XmlRequestFactory(
    private val xmlMapper: XmlMapper,
    private val rootPackage: String
) {
    suspend fun create(call: ApplicationCall): AmazonWebServiceRequest {
        val content: String = call.receiveText().also { logRequest(call, it) }
        val actionName: String = content.drop(1).takeWhile { !it.isWhitespace() }
        val klass: Class<*> = Class.forName("$rootPackage.$actionName")

        val request: AmazonWebServiceRequest = xmlMapper.readValue(content, klass) as AmazonWebServiceRequest
        request.parseAuthorization(call.request)

        return request
    }

    private fun logRequest(call: ApplicationCall, content: String) {
        call.application.log.debug(content)
    }
}
