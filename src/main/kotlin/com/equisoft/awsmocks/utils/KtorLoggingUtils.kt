package com.equisoft.awsmocks.utils

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.RequestAlreadyConsumedException
import io.ktor.request.httpMethod
import io.ktor.request.path
import io.ktor.request.receiveText
import io.ktor.routing.Routing
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import org.slf4j.event.Level
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@OptIn(KtorExperimentalAPI::class)
fun Application.callLogging() {
    install(CallLogging) {
        level = Level.DEBUG
    }

    intercept(ApplicationCallPipeline.Call) {
        log.debug("${call.request.httpMethod} ${call.request.path()}")
        call.request.headers.forEach { key, values ->
            log.debug("$key: ${values.joinToString()}")
        }

        proceed()
    }
}

fun Routing.traceAllCalls() {
    if (application.log.isDebugEnabled) {
        trace { application.log.debug(it.buildText()) }

        receivePipeline.intercept(ApplicationReceivePipeline.Before) { request ->
            cacheRequestBody(request)
        }

        intercept(ApplicationCallPipeline.Call) {
            logRequestBody()
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.logRequestBody() {
    val content = call.receiveText()
    call.application.log.debug("""
            ${call.request.httpMethod} ${call.request.path()}
            ### CONTENT
            $content
            ###
        """.trimIndent())
}

@OptIn(KtorExperimentalAPI::class, ExperimentalStdlibApi::class)
@SuppressWarnings("ThrowsCount")
private suspend fun PipelineContext<ApplicationReceiveRequest, ApplicationCall>.cacheRequestBody(
    request: ApplicationReceiveRequest
) {
    val type: KType = request.typeInfo
    val cachedResult: CachedResult<*>? = call.attributes.getOrNull(LastCachedResult)
    when {
        cachedResult == null -> call.attributes.put(LastCachedResult, RequestAlreadyConsumedResult)
        cachedResult === RequestAlreadyConsumedResult -> throw RequestAlreadyConsumedException()
        cachedResult is CachedResultFailure -> throw cachedResult.cause
        cachedResult is CachedResultSuccess<*> && cachedResult.type == type -> {
            proceedWith(ApplicationReceiveRequest(type, cachedResult))
            return
        }
    }

    try {
        val byteArray = cacheContentAsByteArray(cachedResult, request)
        val incomingContent = byteArray?.let { ByteReadChannel(it) } ?: cachedResult ?: request.value

        proceedWith(ApplicationReceiveRequest(type, incomingContent, reusableValue = true))

        if (cachedResult == null || cachedResult !is CachedResultSuccess) {
            call.attributes.put(LastCachedResult, CachedResultSuccess(typeOf<ByteArray>(), byteArray ?: ByteArray(0)))
        }
    } catch (@SuppressWarnings("TooGenericExceptionCaught") cause: Throwable) {
        call.attributes.put(LastCachedResult, CachedResultFailure(type, cause))
        throw cause
    }
}

@OptIn(ExperimentalStdlibApi::class)
private suspend fun PipelineContext<ApplicationReceiveRequest, ApplicationCall>.cacheContentAsByteArray(
    cachedResult: CachedResult<*>?,
    request: ApplicationReceiveRequest
): ByteArray? {
    var byteArray: ByteArray? = (cachedResult as? CachedResultSuccess<*>)?.value as? ByteArray

    val requestValue: Any = request.value
    if (byteArray == null && requestValue is ByteReadChannel) {
        byteArray = requestValue.toByteArray()
        call.attributes.put(LastCachedResult, CachedResultSuccess(typeOf<ByteArray>(), byteArray))
    }

    return byteArray
}

@OptIn(ExperimentalStdlibApi::class, KtorExperimentalAPI::class)
private val RequestAlreadyConsumedResult =
    CachedResultFailure(typeOf<Any>(), RequestAlreadyConsumedException())

private val LastCachedResult = AttributeKey<CachedResult<*>>("LastCachedResult")
