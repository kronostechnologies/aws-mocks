@file:SuppressWarnings("MagicNumber", "MatchingDeclarationName")

package com.equisoft.awsmocks.common.context

import com.equisoft.awsmocks.DefaultPorts
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.OptionalItem
import com.uchuhimo.konf.source.hocon
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
    addSpec(AwsEnvironment)
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
    val cognito by optional(DefaultPorts.COGNITO)
    val acm by optional(DefaultPorts.ACM)
    val autoScaling by optional(DefaultPorts.AUTOSCALING)
    val kms by optional(DefaultPorts.KMS)
    val ec2 by optional(DefaultPorts.EC2)
    val route53 by optional(DefaultPorts.ROUTE53)
    val elb by optional(DefaultPorts.ELB)
}

object Route53Environment : ConfigSpec("route53") {
    val domain: OptionalItem<String?> by optional(null)
}

object CognitoEnvironment : ConfigSpec("cognito") {
    val issuerHostname: OptionalItem<String> by optional("localhost")
}

object AwsEnvironment : ConfigSpec("aws") {
    val accountId: OptionalItem<String> by optional("123456789012")
}
