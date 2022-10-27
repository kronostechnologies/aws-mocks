package com.equisoft.awsmocks.common.context

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.jackson.JacksonConverter
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.SimpleDateFormat

private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun xmlMapper(): XmlMapper = XmlMapper().configureCommonProperties()

fun objectMapper(): ObjectMapper = jacksonObjectMapper().configureCommonProperties()

fun contentConvertersModule() = module {
    single<ContentConverter>(named("json")) { JacksonConverter(get()) }
    single<ContentConverter>(named("xml")) { JacksonConverter(get<XmlMapper>()) }
}

@Suppress("UNCHECKED_CAST")
private fun <T : ObjectMapper> ObjectMapper.configureCommonProperties(): T =
    registerModules(AfterburnerModule())
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .setDateFormat(SimpleDateFormat(DATE_FORMAT))
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        as T
