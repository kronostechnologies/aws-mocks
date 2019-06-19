package com.equisoft.awsmocks.cognito.infrastructure.cryptography

import com.auth0.jwt.interfaces.RSAKeyProvider
import java.io.InputStream
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateCrtKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec

class RsaKeyProvider(private val keyIdCalculator: KeyIdCalculator) : RSAKeyProvider {
    private val privateKey: RSAPrivateCrtKey = loadPrivateKey()
    private val publicKey: RSAPublicKey = getPublicKey(privateKey)
    private val computedPrivateKeyId: String by lazy { keyIdCalculator.calculate(publicKey) }

    override fun getPublicKeyById(keyId: String?): RSAPublicKey = publicKey

    override fun getPrivateKey(): RSAPrivateKey = privateKey

    override fun getPrivateKeyId(): String = computedPrivateKeyId

    private fun getKeyFactory() = KeyFactory.getInstance("RSA")

    private fun getPublicKey(privateKey: RSAPrivateCrtKey): RSAPublicKey {
        val publicSpec = RSAPublicKeySpec(privateKey.modulus, privateKey.publicExponent)

        return getKeyFactory().generatePublic(publicSpec) as RSAPublicKey
    }

    private fun loadPrivateKey(): RSAPrivateCrtKey {
        val privateSpec = PKCS8EncodedKeySpec(getPrivateKeyBytes())

        return getKeyFactory().generatePrivate(privateSpec) as RSAPrivateCrtKey
    }

    private fun getPrivateKeyBytes(): ByteArray {
        val keyStream: InputStream = javaClass.classLoader.getResourceAsStream("cognito/private_key.der")

        return keyStream.use { it.readBytes() }
    }
}
