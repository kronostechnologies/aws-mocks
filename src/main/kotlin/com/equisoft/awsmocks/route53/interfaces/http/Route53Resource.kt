package com.equisoft.awsmocks.route53.interfaces.http

import com.amazonaws.services.route53.model.ChangeInfo
import com.amazonaws.services.route53.model.ChangeStatus
import com.amazonaws.services.route53.model.CreateReusableDelegationSetRequest
import com.amazonaws.services.route53.model.DeleteReusableDelegationSetRequest
import com.amazonaws.services.route53.model.GetChangeResult
import com.amazonaws.services.route53.model.GetReusableDelegationSetRequest
import com.amazonaws.services.route53.model.ListTagsForResourceRequest
import com.equisoft.awsmocks.common.interfaces.http.XmlRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.getIdParameter
import com.equisoft.awsmocks.route53.application.Route53RequestHandler
import com.equisoft.awsmocks.utils.traceAllCalls
import io.ktor.server.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.Routing
import io.ktor.util.toMap
import org.koin.core.Koin
import java.util.Date

@SuppressWarnings("LongMethod")
fun route53resource(injector: Koin, routing: Routing) {
    routing.apply {
        traceAllCalls()

        route("/2013-04-01") {
            route("/change/{id}") {
                get {
                    val id = getIdParameter()
                    val changeInfo = ChangeInfo(id, ChangeStatus.INSYNC, Date())

                    call.respond(HttpStatusCode.OK, GetChangeResult().withChangeInfo(changeInfo))
                }
            }

            route("/tags/hostedzone/{id}") {
                val requestFactory: XmlRequestFactory by injector.inject()
                val requestHandler: Route53RequestHandler by injector.inject()

                get {
                    val result = requestHandler.handle(ListTagsForResourceRequest(), call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }

                post {
                    val request = requestFactory.create(call)
                    val result = requestHandler.handle(request, call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }
            }

            route("/delegationset") {
                val requestHandler: Route53RequestHandler by injector.inject()

                post {
                    val result = requestHandler.handle(CreateReusableDelegationSetRequest(), call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }

                get("/{id}") {
                    val result = requestHandler.handle(GetReusableDelegationSetRequest(), call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }

                delete("/{id}") {
                    val result = requestHandler.handle(DeleteReusableDelegationSetRequest(), call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }
            }

            hostedZoneResource(injector, this)
        }
    }
}
