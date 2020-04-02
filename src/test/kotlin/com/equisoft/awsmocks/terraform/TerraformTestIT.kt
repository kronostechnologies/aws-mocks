package com.equisoft.awsmocks.terraform

import com.equisoft.awsmocks.main
import com.equisoft.awsmocks.testutils.StdPrinter
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.MountableFile

@Tag("e2e")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TerraformTestIT {
    companion object {
        @Container
        @JvmStatic
        val terraform = TerraformContainer()
    }

    @BeforeAll
    fun beforeAll() {
        main()

        val workingDirectory = "/app/infra"
        terraform.withCreateContainerCmdModifier { it.withEntrypoint("./provision.sh") }
            .withWorkingDirectory(workingDirectory)
            .withCopyFileToContainer(MountableFile.forClasspathResource("infra", 777), workingDirectory)
            .withLogConsumer(StdPrinter())
    }

    @Test
    fun `should provision with terraform`() {
        terraform.start()
        runBlocking {
            terraform.waitForApply()
        }
    }

    @AfterAll
    fun afterAll() {
        terraform.stop()
    }
}
