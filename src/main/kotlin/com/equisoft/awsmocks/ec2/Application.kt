package com.equisoft.awsmocks.ec2

import com.equisoft.awsmocks.common.context.startLocalKoin
import com.equisoft.awsmocks.common.exceptions.ErrorResponse
import com.equisoft.awsmocks.common.exceptions.NotFoundError
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.installContentNegotiation
import com.equisoft.awsmocks.ec2.context.ec2Modules
import com.equisoft.awsmocks.ec2.interfaces.http.ec2Resource
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

fun Application.ec2() {
    koin?.close()
    koin = startLocalKoin(ec2Modules()).also { init(it.koin) }
}

private fun Application.init(injector: Koin) {
    callLogging()

    installContentNegotiation(injector, ContentType.Application.Xml)

    install(StatusPages) {
        exception<NotFoundException> {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(NotFoundError(it.errorCode)))
        }
    }

    routing {
        ec2Resource(injector, this)
    }
}
