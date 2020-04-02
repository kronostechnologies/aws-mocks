package com.equisoft.awsmocks.terraform

import com.equisoft.awsmocks.testutils.TerraformApplyFailed
import com.github.dockerjava.api.model.HostConfig
import kotlinx.coroutines.delay
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.OutputFrame
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

private const val FIVE_MINUTES_MS = 300_000L
private val TERRAFORM_VERSION: String = System.getenv("TERRAFORM_VERSION") ?: "0.12.24"

class TerraformContainer : GenericContainer<TerraformContainer>("hashicorp/terraform:$TERRAFORM_VERSION") {
    init {
        withEnv("TF_VAR_hostname", "host.docker.internal")
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
