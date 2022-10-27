package com.equisoft.awsmocks.common

import com.equisoft.awsmocks.common.interfaces.http.feature.ResponseLogging
import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.core.Koin
import org.koin.core.qualifier.named

fun Application.installContentNegotiation(injector: Koin, mainContentType: ContentType) {
    lateinit var anyContentConverter: ContentConverter
    val jsonContentConverter: ContentConverter = injector.get(named("json"))
    val xmlContentConverter: ContentConverter = injector.get(named("xml"))

    install(ContentNegotiation) {
        if (mainContentType.match(ContentType.Application.Json)) {
            anyContentConverter = jsonContentConverter
            register(ContentType.Any, jsonContentConverter)
            register(ContentType.Application.Json, jsonContentConverter)
            register(ContentType.Application.Xml, xmlContentConverter)
        } else {
            anyContentConverter = xmlContentConverter
            register(ContentType.Any, xmlContentConverter)
            register(ContentType.Application.Xml, xmlContentConverter)
            register(ContentType.Application.Json, jsonContentConverter)
        }
    }

    install(ResponseLogging) {
        this.anyContentConverter = anyContentConverter
        this.jsonContentConverter = jsonContentConverter
        this.xmlContentConverter = xmlContentConverter
    }
}
