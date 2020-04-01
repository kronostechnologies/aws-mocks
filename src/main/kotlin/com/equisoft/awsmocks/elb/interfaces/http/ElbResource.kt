package com.equisoft.awsmocks.elb.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.equisoft.awsmocks.common.interfaces.http.FormRequestFactory
import com.equisoft.awsmocks.elb.application.ElbRequestHandler
import com.equisoft.awsmocks.utils.traceAllCalls
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import org.koin.core.Koin

@SuppressWarnings("LongMethod")
fun elbResource(injector: Koin, routing: Routing) {
    routing.apply {
        traceAllCalls()

        val requestFactory: FormRequestFactory by injector.inject()
        val requestHandler: ElbRequestHandler by injector.inject()

        post {
            val request: AmazonWebServiceRequest = requestFactory.create(call)
            val result: AmazonWebServiceResult<ResponseMetadata> = requestHandler.handle(request)

            call.respond(HttpStatusCode.OK, result)
        }
    }
}
