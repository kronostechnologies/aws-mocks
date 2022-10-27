package com.equisoft.awsmocks.acm.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.equisoft.awsmocks.acm.application.AcmRequestHandler
import com.equisoft.awsmocks.common.interfaces.http.JsonRequestFactory
import com.equisoft.awsmocks.utils.traceAllCalls
import io.ktor.server.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.koin.core.Koin

fun acmResource(injector: Koin, routing: Routing) {
    routing.apply {
        traceAllCalls()

        val requestFactory: JsonRequestFactory by injector.inject()
        val requestHandler: AcmRequestHandler by injector.inject()

        post {
            val request: AmazonWebServiceRequest = requestFactory.create(call)
            val result: AmazonWebServiceResult<ResponseMetadata> = requestHandler.handle(request)

            call.respond(HttpStatusCode.OK, result)
        }
    }
}
