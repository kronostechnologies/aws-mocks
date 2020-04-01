package com.equisoft.awsmocks

import com.equisoft.awsmocks.acm.acm
import com.equisoft.awsmocks.autoscaling.autoScaling
import com.equisoft.awsmocks.cognito.cognito
import com.equisoft.awsmocks.common.context.PortsEnvironment
import com.equisoft.awsmocks.common.context.applicationConfig
import com.equisoft.awsmocks.ec2.ec2
import com.equisoft.awsmocks.ecs.ecs
import com.equisoft.awsmocks.elb.elb
import com.equisoft.awsmocks.kms.kms
import com.equisoft.awsmocks.route53.route53
import io.ktor.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("com.equisoft.awsmocks")

@SuppressWarnings("LongMethod")
fun main() {
    runBlocking {
        listOf(
            Application::acm to applicationConfig[PortsEnvironment.acm],
            Application::autoScaling to applicationConfig[PortsEnvironment.autoScaling],
            Application::cognito to applicationConfig[PortsEnvironment.cognito],
            Application::kms to applicationConfig[PortsEnvironment.kms],
            Application::ec2 to applicationConfig[PortsEnvironment.ec2],
            Application::ecs to applicationConfig[PortsEnvironment.ecs],
            Application::elb to applicationConfig[PortsEnvironment.elb],
            Application::route53 to applicationConfig[PortsEnvironment.route53]
        ).map {
            launch {
                val name = it.first.name
                LOGGER.info("Starting ${name.capitalize()} mock")

                embeddedServer(
                    Jetty,
                    environment = applicationEngineEnvironment {
                        connector {
                            port = it.second
                        }

                        log = LoggerFactory.getLogger(name)
                        module(it.first)
                        watchPaths = listOf("common", name)
                    }
                ).start()
            }
        }
    }
}
