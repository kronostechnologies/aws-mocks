package com.equisoft.awsmocks.common.utils

fun <T> Int.mapEach(block: (Int) -> T): List<T> = (0 until this).map { block(it) }

fun <T> Boolean?.mapTrue(block: () -> T?) = if (this == true) block() else null
