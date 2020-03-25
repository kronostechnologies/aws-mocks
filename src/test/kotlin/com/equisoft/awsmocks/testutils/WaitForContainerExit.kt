package com.equisoft.awsmocks.testutils

import org.testcontainers.containers.ContainerState
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy

private const val FIVE_MINUTES_MS = 300_000L

class WaitForContainerExit(
    private val container: ContainerState,
    private val maxDelayMs: Long = FIVE_MINUTES_MS
) : AbstractWaitStrategy() {
    override fun waitUntilReady() {
        val expiry = System.currentTimeMillis() + maxDelayMs

        while (System.currentTimeMillis() < expiry) {
            Thread.sleep(1000L)

            @Suppress("UsePropertyAccessSyntax")
            if (container.getContainerId() != null && !container.isRunning) {
                return
            }
        }
    }
}
