package com.equisoft.awsmocks.cognito.infrastructure.cryptography

import com.nimbusds.jose.jwk.KeyType
import com.nimbusds.jose.jwk.ThumbprintUtils
import java.security.interfaces.RSAPublicKey
import java.util.LinkedHashMap

class KeyIdCalculator {
    fun calculate(publicKey: RSAPublicKey): String {
        val requiredParams = LinkedHashMap<String, String>()
        requiredParams["e"] = publicKey.publicExponent.toString()
        requiredParams["kty"] = KeyType.RSA.value
        requiredParams["n"] = publicKey.modulus.toString()

        return ThumbprintUtils.compute("SHA-256", requiredParams).toString()
    }
}
