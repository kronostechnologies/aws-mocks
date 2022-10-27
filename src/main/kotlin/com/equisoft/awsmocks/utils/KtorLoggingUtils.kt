package com.equisoft.awsmocks.utils

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import io.ktor.util.pipeline.PipelineContext
import org.slf4j.event.Level

fun Application.callLogging() {
    install(DoubleReceive)
    install(CallLogging) {
        level = Level.DEBUG
    }

    intercept(ApplicationCallPipeline.Call) {
        this@callLogging.log.debug("${call.request.httpMethod} ${call.request.path()}")
        call.request.headers.forEach { key, values ->
            this@callLogging.log.debug("$key: ${values.joinToString()}")
        }

        proceed()
    }
}

fun Routing.traceAllCalls() {
    if (application.log.isDebugEnabled) {
        trace { application.log.debug(it.buildText()) }

        intercept(ApplicationCallPipeline.Call) {
            logRequestBody()
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.logRequestBody() {
    val content = call.receiveText()
    call.application.log.debug(
        """
            ${call.request.httpMethod} ${call.request.path()}
            ### CONTENT
            $content
            ###
        """.trimIndent()
    )
}
