package com.equisoft.awsmocks.common.interfaces.http.feature

import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.features.ContentNegotiation.ConverterRegistration
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.response.ApplicationSendPipeline
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext

class ResponseLogging {
    class Configuration {
        var contentNegotiation: ContentNegotiation? = null
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, ResponseLogging> {
        override val key = AttributeKey<ResponseLogging>("ResponseLogging")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): ResponseLogging {
            val configuration = Configuration().apply(configure)
            val contentNegotiation: ContentNegotiation = checkNotNull(configuration.contentNegotiation)

            pipeline.sendPipeline.intercept(ApplicationSendPipeline.Transform) {
                if (subject is OutgoingContent) return@intercept

                findConverterForContentType(contentNegotiation.registrations)?.apply {
                    call.application.log.debug(convertToString(this@intercept, subject))
                }

                proceed()
            }

            return ResponseLogging()
        }
    }
}

private fun PipelineContext<Any, ApplicationCall>.findConverterForContentType(
    registrations: List<ConverterRegistration>
): ConverterRegistration? {
    val contentType: String? = call.request.headers[HttpHeaders.ContentType]

    return if (contentType?.contains("json") == true) {
        registrations.find(ContentType.Application.Json)
    } else {
        registrations.find(ContentType.Any)
    }
}

private fun List<ConverterRegistration>.find(contentType: ContentType): ConverterRegistration? = find {
    it.contentType.match(contentType)
} ?: if (contentType == ContentType.Any) null else find(ContentType.Any)

private suspend fun ConverterRegistration.convertToString(
    context: PipelineContext<Any, ApplicationCall>,
    value: Any
): String? =
    when (val content: Any? = converter.convertForSend(context, contentType, value)) {
        is TextContent -> content.text
        else -> content.toString()
    }
