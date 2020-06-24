package com.equisoft.awsmocks.common

import com.equisoft.awsmocks.common.interfaces.http.feature.ResponseLogging
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import org.koin.core.Koin
import org.koin.core.qualifier.named

fun Application.installContentNegotiation(injector: Koin, mainContentType: ContentType) {
    val contentNegotiationFeature: ContentNegotiation = install(ContentNegotiation) {
        if (mainContentType.match(ContentType.Application.Json)) {
            register(ContentType.Any, injector.get(named("json")))
            register(ContentType.Application.Json, injector.get(named("json")))
            register(ContentType.Application.Xml, injector.get(named("xml")))
        } else {
            register(ContentType.Any, injector.get(named("xml")))
            register(ContentType.Application.Xml, injector.get(named("xml")))
            register(ContentType.Application.Json, injector.get(named("json")))
        }
    }

    install(ResponseLogging) {
        contentNegotiation = contentNegotiationFeature
    }
}
