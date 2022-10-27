package com.equisoft.awsmocks.common.interfaces.http.feature

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.ContentConverter
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.response.ApplicationSendPipeline
import io.ktor.server.response.responseType
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext

class ResponseLogging {
    class Configuration {
        var anyContentConverter: ContentConverter? = null
        var jsonContentConverter: ContentConverter? = null
        var xmlContentConverter: ContentConverter? = null
    }

    companion object Feature : BaseApplicationPlugin<ApplicationCallPipeline, Configuration, ResponseLogging> {
        override val key = AttributeKey<ResponseLogging>("ResponseLogging")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): ResponseLogging {
            val configuration = Configuration().apply(configure)

            pipeline.sendPipeline.intercept(ApplicationSendPipeline.Transform) {
                if (subject is OutgoingContent) return@intercept

                findConverterForContentType(configuration)?.run {
                    call.application.log.debug(convertToString(this@intercept, subject))
                }

                proceed()
            }

            return ResponseLogging()
        }
    }
}

private fun PipelineContext<Any, ApplicationCall>.findConverterForContentType(
    configuration: ResponseLogging.Configuration,
): ContentConverter? {
    val contentType: String? = call.request.headers[HttpHeaders.ContentType]

    return if (contentType?.contains("json") == true) {
        configuration.anyContentConverter
    } else {
        configuration.jsonContentConverter
    }
}

private suspend fun ContentConverter.convertToString(
    context: PipelineContext<Any, ApplicationCall>,
    value: Any
): String? {
    val contentType: String =
        context.call.request.headers[HttpHeaders.ContentType] ?: ContentType.Application.Json.toString()

    val responseType = context.call.response.responseType ?: return null

    return when (val content: Any? = this.serializeNullable(
        ContentType.parse(contentType),
        Charsets.UTF_8,
        responseType,
        value
    )) {
        is OutgoingContent.ByteArrayContent -> content.bytes().decodeToString()
        else -> content.toString()
    }
}
