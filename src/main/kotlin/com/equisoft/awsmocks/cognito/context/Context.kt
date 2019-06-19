package com.equisoft.awsmocks.cognito.context

import com.auth0.jwt.interfaces.RSAKeyProvider
import com.equisoft.awsmocks.cognito.application.CognitoRequestHandler
import com.equisoft.awsmocks.cognito.application.OAuthService
import com.equisoft.awsmocks.cognito.application.Token
import com.equisoft.awsmocks.cognito.application.UserPoolService
import com.equisoft.awsmocks.cognito.infrastructure.cryptography.JwksProvider
import com.equisoft.awsmocks.cognito.infrastructure.cryptography.KeyIdCalculator
import com.equisoft.awsmocks.cognito.infrastructure.cryptography.RsaKeyProvider
import com.equisoft.awsmocks.cognito.infrastructure.persistence.ResourceServerRepository
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolClientRepository
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolDomainRepository
import com.equisoft.awsmocks.cognito.infrastructure.persistence.UserPoolRepository
import com.equisoft.awsmocks.cognito.interfaces.http.serialization.jackson.TokenMixin
import com.equisoft.awsmocks.common.context.configModule
import com.equisoft.awsmocks.common.context.contentConvertersModule
import com.equisoft.awsmocks.common.context.objectMapper
import com.equisoft.awsmocks.common.context.xmlMapper
import com.equisoft.awsmocks.common.interfaces.http.JsonRequestFactory
import org.koin.core.module.Module
import org.koin.dsl.module

fun cognitoModules(): List<Module> {
    return listOf(module {
        single { OAuthService(get(), get(), get()) }
        single { UserPoolService(get(), get(), get(), get()) }

        single { UserPoolRepository() }
        single { UserPoolClientRepository() }
        single { UserPoolDomainRepository() }
        single { ResourceServerRepository() }

        single { CognitoRequestHandler(get()) }
        single { JsonRequestFactory(get(), "com.amazonaws.services.cognitoidp.model") }

        single { xmlMapper() }
        single { objectMapper().addMixIn(Token::class.java, TokenMixin::class.java) }

        single<RSAKeyProvider> { RsaKeyProvider(get()) }
        single { JwksProvider(get()) }
        single { KeyIdCalculator() }
    }, contentConvertersModule(), configModule())
}
