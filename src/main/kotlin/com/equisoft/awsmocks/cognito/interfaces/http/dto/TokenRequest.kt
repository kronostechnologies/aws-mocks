package com.equisoft.awsmocks.cognito.interfaces.http.dto

import io.ktor.http.Parameters
import io.ktor.server.auth.UserPasswordCredential

data class TokenRequest(
    val userPoolId: String,
    val grantType: String,
    val clientId: String,
    val clientSecret: String?,
    val scope: String?,
    val redirectUri: String?,
    val refreshToken: String?,
    val code: String?,
    val codeVerifier: String?
)

fun Parameters.toTokenRequest(userPoolId: String, credentials: UserPasswordCredential?): TokenRequest = TokenRequest(
    userPoolId,
    checkNotNull(get("grant_type")),
    checkNotNull(credentials?.name ?: get("client_id")),
    credentials?.password,
    get("scope"),
    get("redirect_uri"),
    get("refresh_token"),
    get("code"),
    get("code_verifier")
)
