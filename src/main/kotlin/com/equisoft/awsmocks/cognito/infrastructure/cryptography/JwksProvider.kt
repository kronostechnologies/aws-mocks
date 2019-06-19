package com.equisoft.awsmocks.cognito.infrastructure.cryptography

import com.auth0.jwt.interfaces.RSAKeyProvider
import com.nimbusds.jose.Algorithm
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey

private const val ANY_KEY = ""

class JwksProvider(private val rsaKeyProvider: RSAKeyProvider) {
    private val jwkJson: String by lazy {
        val jwk: RSAKey = RSAKey.Builder(rsaKeyProvider.getPublicKeyById(ANY_KEY))
            .algorithm(Algorithm("RS256"))
            .keyUse(KeyUse.SIGNATURE)
            .keyID(rsaKeyProvider.privateKeyId)
            .build()
            .toPublicJWK()

        jwk.toJSONString()
    }

    fun get(): String = """
        {
            "keys": [
                $jwkJson
            ]
        }
    """.trimIndent()
}
