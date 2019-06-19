package com.equisoft.awsmocks.utils

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.request.httpMethod
import io.ktor.request.path
import io.ktor.request.receiveText
import io.ktor.routing.Routing
import io.ktor.routing.route
import org.slf4j.event.Level

fun Application.callLogging() {
    install(CallLogging) {
        level = Level.DEBUG
    }

    intercept(ApplicationCallPipeline.Call) {
        call.application.log.debug("${call.request.httpMethod} ${call.request.path()}")
        call.request.headers.forEach { key, values ->
            call.application.log.debug("$key: ${values.joinToString()}")
        }

        proceed()
    }
}

fun Routing.traceAllCalls() {
    trace { application.log.debug(it.buildText()) }

    route("{path...}") {
        handle {
            application.log.warn("""
                    ${call.request.httpMethod} ${call.request.path()}
                    ### CONTENT
                    ${call.receiveText()}
                    ###
                """.trimIndent()
            )
        }
    }
}
