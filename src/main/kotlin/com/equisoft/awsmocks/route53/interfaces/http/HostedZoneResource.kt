package com.equisoft.awsmocks.route53.interfaces.http

import com.amazonaws.services.route53.model.GetHostedZoneResult
import com.amazonaws.services.route53.model.HostedZone
import com.amazonaws.services.route53.model.ListHostedZonesRequest
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest
import com.equisoft.awsmocks.common.interfaces.http.XmlRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.getIdParameter
import com.equisoft.awsmocks.route53.application.HostedZoneService
import com.equisoft.awsmocks.route53.application.Route53RequestHandler
import com.equisoft.awsmocks.route53.application.getHostedZoneResult
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.toMap
import org.koin.core.Koin

@SuppressWarnings("LongMethod")
fun hostedZoneResource(injector: Koin, parentRoute: Route) {
    parentRoute {
        val hostedZoneService: HostedZoneService by injector.inject()
        val requestFactory: XmlRequestFactory by injector.inject()
        val requestHandler: Route53RequestHandler by injector.inject()

        route("/hostedzone") {
            get {
                val result = requestHandler.handle(ListHostedZonesRequest(), call.parameters.toMap())
                call.respond(HttpStatusCode.OK, result)
            }

            post {
                val request = requestFactory.create(call)
                val result = requestHandler.handle(request, call.parameters.toMap())

                call.respond(HttpStatusCode.Created, result)
            }

            route("/{id}") {
                get {
                    val hostedZone: HostedZone = hostedZoneService.get(getIdParameter())
                    val hostedZoneResult: GetHostedZoneResult = getHostedZoneResult(hostedZone)

                    call.respond(HttpStatusCode.OK, hostedZoneResult)
                }

                post {
                    val request = requestFactory.create(call)
                    val result = requestHandler.handle(request, call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }

                delete {
                    hostedZoneService.delete(getIdParameter())
                    call.respond(HttpStatusCode.OK)
                }

                route("/rrset") {
                    get {
                        val result = requestHandler.handle(ListResourceRecordSetsRequest(), call.parameters.toMap())

                        call.respond(HttpStatusCode.OK, result)
                    }

                    post {
                        val request = requestFactory.create(call)
                        val result = requestHandler.handle(request, call.parameters.toMap())

                        call.respond(HttpStatusCode.OK, result)
                    }
                }
            }
        }
    }
}
