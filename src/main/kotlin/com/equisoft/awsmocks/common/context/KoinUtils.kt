package com.equisoft.awsmocks.common.context

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.logger.SLF4JLogger

fun startLocalKoin(modules: List<Module>): KoinApplication = koinApplication {
    logger(SLF4JLogger())
    modules(modules)
    createEagerInstances()
}
