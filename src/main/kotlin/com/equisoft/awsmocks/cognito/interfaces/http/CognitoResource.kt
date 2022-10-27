package com.equisoft.awsmocks.cognito.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.equisoft.awsmocks.cognito.application.CognitoRequestHandler
import com.equisoft.awsmocks.cognito.application.OAuthService
import com.equisoft.awsmocks.cognito.application.Token
import com.equisoft.awsmocks.cognito.infrastructure.cryptography.JwksProvider
import com.equisoft.awsmocks.cognito.interfaces.http.dto.TokenRequest
import com.equisoft.awsmocks.cognito.interfaces.http.dto.toTokenRequest
import com.equisoft.awsmocks.common.interfaces.http.JsonRequestFactory
import com.equisoft.awsmocks.utils.traceAllCalls
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.basicAuthenticationCredentials
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.core.Koin

fun cognitoResource(injector: Koin, routing: Routing) {
    routing.apply {
        traceAllCalls()

        provisioningResource(injector)

        oauthResource(injector)
    }
}

private fun Routing.oauthResource(injector: Koin) {
    val oAuthService: OAuthService by injector.inject()

    get("{userPoolId}/.well-known/jwks.json") {
        val jwksProvider: JwksProvider by injector.inject()

        call.respondText(jwksProvider.get(), ContentType.Application.Json)
    }

    post("{userPoolId}/oauth2/token") {
        @Suppress("EXPERIMENTAL_API_USAGE")
        val credentials: UserPasswordCredential? = call.request.basicAuthenticationCredentials()

        val tokenRequest: TokenRequest = call.receiveParameters()
            .toTokenRequest(call.parameters["userPoolId"]!!, credentials)

        val token: Token = oAuthService.createAccessToken(tokenRequest)
        call.respond(token)
    }
}

private fun Routing.provisioningResource(injector: Koin) {
    val requestFactory: JsonRequestFactory by injector.inject()
    val requestHandler: CognitoRequestHandler by injector.inject()

    post {
        val request: AmazonWebServiceRequest = requestFactory.create(call)
        val result: AmazonWebServiceResult<ResponseMetadata> = requestHandler.handle(request)

        call.respond(HttpStatusCode.OK, result)
    }
}
