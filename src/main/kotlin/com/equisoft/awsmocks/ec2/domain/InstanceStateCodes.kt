package com.equisoft.awsmocks.ec2.domain

class InstanceStateCodes private constructor() {
    companion object {
        const val RUNNING = 16
        const val SHUTTING_DOWN = 32
        const val TERMINATED = 48
    }
}
