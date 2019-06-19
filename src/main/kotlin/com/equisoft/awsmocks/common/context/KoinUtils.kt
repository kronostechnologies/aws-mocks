package com.equisoft.awsmocks.common.context

import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun startLocalKoin(modules: List<Module>): KoinApplication = KoinApplication.create().apply {
    modules(modules)
    createEagerInstances()
}
