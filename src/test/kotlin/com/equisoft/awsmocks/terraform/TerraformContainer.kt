package com.equisoft.awsmocks.terraform

import com.github.dockerjava.api.model.HostConfig
import org.testcontainers.containers.GenericContainer

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
}
