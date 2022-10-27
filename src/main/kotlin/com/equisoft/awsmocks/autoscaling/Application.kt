package com.equisoft.awsmocks.autoscaling

import com.equisoft.awsmocks.autoscaling.context.autoScalingModules
import com.equisoft.awsmocks.autoscaling.interfaces.http.autoScalingResource
import com.equisoft.awsmocks.common.context.startLocalKoin
import com.equisoft.awsmocks.common.exceptions.BadRequestError
import com.equisoft.awsmocks.common.exceptions.BadRequestException
import com.equisoft.awsmocks.common.exceptions.ErrorResponse
import com.equisoft.awsmocks.common.exceptions.NotFoundError
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.installContentNegotiation
import com.equisoft.awsmocks.utils.callLogging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import org.koin.core.Koin
import org.koin.core.KoinApplication

private var koin: KoinApplication? = null

fun Application.autoScaling() {
    koin?.close()
    koin = startLocalKoin(autoScalingModules()).also { init(it.koin) }
}

private fun Application.init(injector: Koin) {
    callLogging()

    installContentNegotiation(injector, ContentType.Application.Xml)

    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(BadRequestError(cause.errorCode)))
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(NotFoundError(cause.errorCode)))
        }
    }

    routing {
        autoScalingResource(injector, this)
    }
}
