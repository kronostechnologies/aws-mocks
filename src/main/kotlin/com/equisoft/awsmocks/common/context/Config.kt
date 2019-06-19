@file:SuppressWarnings("MagicNumber", "MatchingDeclarationName")

package com.equisoft.awsmocks.common.context

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.OptionalItem
import org.koin.dsl.module

private const val ENV_FILENAME = "application.conf"
private const val DEV_ENV_FILENAME = "application.dev.conf"

val applicationConfig: Config by lazy { loadConfig() }

fun configModule() = module {
    single { applicationConfig }
}

@SuppressWarnings("TooGenericExceptionCaught")
private fun loadConfig(): Config = Config {
    addSpec(PortsEnvironment)
    addSpec(Route53Environment)
    addSpec(CognitoEnvironment)
}
    .from.hocon.resource(ENV_FILENAME)
    .let { conf ->
        try {
            conf.from.hocon.resource(DEV_ENV_FILENAME)
        } catch (e: Exception) {
            conf
        }
    }
    .from.env()

object PortsEnvironment : ConfigSpec("ports") {
    val cognito by optional(4500)
    val acm by optional(4501)
    val kms by optional(4502)
    val ec2 by optional(4503)
    val route53 by optional(4580)
}

object Route53Environment : ConfigSpec("route53") {
    val domain: OptionalItem<String?> by optional(null)
}

object CognitoEnvironment : ConfigSpec("cognito") {
    val issuerHostname: OptionalItem<String> by optional("localhost")
}
