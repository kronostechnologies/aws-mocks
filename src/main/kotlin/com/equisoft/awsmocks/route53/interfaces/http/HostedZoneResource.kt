package com.equisoft.awsmocks.route53.interfaces.http

import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneRequest
import com.amazonaws.services.route53.model.DelegationSet
import com.amazonaws.services.route53.model.DisassociateVPCFromHostedZoneRequest
import com.amazonaws.services.route53.model.GetHostedZoneResult
import com.amazonaws.services.route53.model.HostedZone
import com.amazonaws.services.route53.model.ListHostedZonesRequest
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest
import com.equisoft.awsmocks.common.interfaces.http.XmlRequestFactory
import com.equisoft.awsmocks.common.interfaces.http.getIdParameter
import com.equisoft.awsmocks.route53.application.DelegationSetService
import com.equisoft.awsmocks.route53.application.HostedZoneService
import com.equisoft.awsmocks.route53.application.Route53RequestHandler
import com.equisoft.awsmocks.route53.application.VpcAssociationService
import com.equisoft.awsmocks.route53.application.getHostedZoneResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.toMap
import org.koin.core.Koin

@SuppressWarnings("LongMethod")
fun hostedZoneResource(injector: Koin, parentRoute: Route) {
    parentRoute {
        val hostedZoneService: HostedZoneService by injector.inject()
        val delegationSetService: DelegationSetService by injector.inject()
        val vpcAssociationService: VpcAssociationService by injector.inject()
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
                    val delegationSet: DelegationSet? = delegationSetService.findForZone(hostedZone)
                    val hostedZoneResult: GetHostedZoneResult = getHostedZoneResult(hostedZone)
                        .withDelegationSet(delegationSet)
                        .withVPCs(vpcAssociationService.getAll(hostedZone.id))

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

                post("/associatevpc") {
                    val result = requestHandler.handle(AssociateVPCWithHostedZoneRequest(), call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }

                post("/disassociatevpc ") {
                    val result = requestHandler.handle(DisassociateVPCFromHostedZoneRequest(), call.parameters.toMap())

                    call.respond(HttpStatusCode.OK, result)
                }

                route("/rrset") {
                    get {
                        val result = requestHandler.handle(ListResourceRecordSetsRequest(), call.parameters.toMap())

                        call.respond(HttpStatusCode.OK, result)
                    }

                    post("/") {
                        val request = requestFactory.create(call)
                        val result = requestHandler.handle(request, call.parameters.toMap())

                        call.respond(HttpStatusCode.OK, result)
                    }
                }
            }
        }
    }
}
