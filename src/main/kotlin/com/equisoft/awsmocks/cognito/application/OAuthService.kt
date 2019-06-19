package com.equisoft.awsmocks.cognito.application

import com.amazonaws.services.cognitoidp.model.UserPoolClientType
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolClientRepository
import com.equisoft.awsmocks.cognito.interfaces.http.dto.TokenRequest
import com.equisoft.awsmocks.common.context.CognitoEnvironment
import com.equisoft.awsmocks.common.context.PortsEnvironment
import com.equisoft.awsmocks.common.exceptions.BadRequestException
import com.uchuhimo.konf.Config
import java.time.Instant
import java.util.Date
import java.util.UUID

private const val TOKEN_EXPIRY_IN_SECONDS: Long = 7_200L

class OAuthService(
    private val config: Config,
    private val userPoolClientRepository: UserPoolClientRepository,
    private val rsaKeyProvider: RSAKeyProvider
) {
    fun createAccessToken(tokenRequest: TokenRequest): Token {
        val userPoolClient: UserPoolClientType = getValidUserPoolClient(tokenRequest)
        val scopes: List<String> = filterAllowedScopes(tokenRequest.scope, userPoolClient.allowedOAuthScopes)
        val accessToken: String = createJwtToken(tokenRequest, scopes)

        return Token(accessToken, TOKEN_EXPIRY_IN_SECONDS)
    }

    private fun getValidUserPoolClient(tokenRequest: TokenRequest): UserPoolClientType {
        val userPoolClient: UserPoolClientType? = userPoolClientRepository.find(tokenRequest.userPoolId,
            tokenRequest.clientId)

        if (userPoolClient == null || userPoolClient.clientSecret != tokenRequest.clientSecret) {
            throw BadRequestException()
        }

        return userPoolClient
    }

    private fun createJwtToken(tokenRequest: TokenRequest, scopes: List<String>): String {
        val hostname: String = config[CognitoEnvironment.issuerHostname]
        val port: Int = config[PortsEnvironment.cognito]

        return JWT.create()
            .withSubject(tokenRequest.clientId)
            .withIssuedAt(Date())
            .withExpiresAt(Date.from(Instant.now().plusSeconds(TOKEN_EXPIRY_IN_SECONDS)))
            .withIssuer("http://$hostname:$port/${tokenRequest.userPoolId}")
            .withClaim("token_use", "access")
            .withClaim("scope", scopes.joinToString(" "))
            .withClaim("client_id", tokenRequest.clientId)
            .withJWTId(UUID.randomUUID().toString())
            .sign(Algorithm.RSA256(rsaKeyProvider))
    }

    private fun filterAllowedScopes(requestScopes: String?, allowedOAuthScopes: List<String>): List<String> {
        if (requestScopes.isNullOrBlank()) {
            return allowedOAuthScopes
        }

        return requestScopes.split(" ")
            .filter { it in allowedOAuthScopes }
            .ifEmpty { throw BadRequestException() }
    }
}
