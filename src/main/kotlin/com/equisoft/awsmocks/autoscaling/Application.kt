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
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
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
        exception<BadRequestException> {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(BadRequestError(it.errorCode)))
        }
        exception<NotFoundException> {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(NotFoundError(it.errorCode)))
        }
    }

    routing {
        autoScalingResource(injector, this)
    }
}
