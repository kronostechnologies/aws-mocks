package com.equisoft.awsmocks.kms

import com.equisoft.awsmocks.common.context.startLocalKoin
import com.equisoft.awsmocks.common.exceptions.NotFoundError
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.exceptions.ResourceNotFoundError
import com.equisoft.awsmocks.common.exceptions.ResourceNotFoundException
import com.equisoft.awsmocks.common.installContentNegotiation
import com.equisoft.awsmocks.kms.context.kmsModules
import com.equisoft.awsmocks.kms.interfaces.http.kmsResource
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

fun Application.kms() {
    koin?.close()
    koin = startLocalKoin(kmsModules()).also { init(it.koin) }
}

private fun Application.init(injector: Koin) {
    callLogging()

    installContentNegotiation(injector, ContentType.Application.Json)

    install(StatusPages) {
        exception<ResourceNotFoundException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ResourceNotFoundError)
        }
        exception<NotFoundException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, NotFoundError())
        }
    }

    routing {
        kmsResource(injector, this)
    }
}
