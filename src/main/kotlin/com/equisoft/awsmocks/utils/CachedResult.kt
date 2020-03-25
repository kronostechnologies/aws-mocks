package com.equisoft.awsmocks.utils

import kotlin.reflect.KType

sealed class CachedResult<T : Any>(val type: KType)
class CachedResultSuccess<T : Any>(type: KType, val value: T) : CachedResult<T>(type)
class CachedResultFailure(type: KType, val cause: Throwable) : CachedResult<Nothing>(type)
