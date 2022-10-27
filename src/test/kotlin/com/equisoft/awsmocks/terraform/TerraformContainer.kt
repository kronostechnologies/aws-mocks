package com.equisoft.awsmocks.terraform

import com.equisoft.awsmocks.DefaultPorts
import com.equisoft.awsmocks.testutils.TerraformApplyFailed
import com.github.dockerjava.api.model.HostConfig
import kotlinx.coroutines.delay
import org.testcontainers.Testcontainers
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.OutputFrame
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

private const val FIVE_MINUTES_MS = 300_000L
private val TERRAFORM_VERSION: String = System.getenv("TERRAFORM_VERSION") ?: "1.3.3"

class TerraformContainer : GenericContainer<TerraformContainer>("hashicorp/terraform:$TERRAFORM_VERSION") {
    init {
        Testcontainers.exposeHostPorts(
            DefaultPorts.ACM,
            DefaultPorts.AUTOSCALING,
            DefaultPorts.COGNITO,
            DefaultPorts.EC2,
            DefaultPorts.ECS,
            DefaultPorts.ELB,
            DefaultPorts.KMS,
            DefaultPorts.ROUTE53
        )
        withEnv("TF_VAR_hostname", INTERNAL_HOST_HOSTNAME)
            .withCommand("apply -auto-approve")
            .withCreateContainerCmdModifier {
                val hostConfig: HostConfig = it.hostConfig ?: HostConfig.newHostConfig()
                hostConfig.apply {
                    withMemory(256 * 1024 * 1024)
                }
            }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun waitForApply(maxDelayMs: Long = FIVE_MINUTES_MS) {
        val expiry = System.currentTimeMillis() + maxDelayMs

        while (System.currentTimeMillis() < expiry) {
            delay(1.seconds)

            @Suppress("UsePropertyAccessSyntax")
            if (getContainerId() != null && !isRunning) {
                val errorLogs = getLogs(OutputFrame.OutputType.STDERR)
                if (!errorLogs.isNullOrEmpty()) {
                    throw TerraformApplyFailed(errorLogs)
                }

                return
            }
        }
    }
}
