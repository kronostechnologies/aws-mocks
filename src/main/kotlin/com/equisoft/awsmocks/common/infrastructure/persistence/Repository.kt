package com.equisoft.awsmocks.common.infrastructure.persistence

import java.util.concurrent.ConcurrentMap

interface Repository<K, V> : ConcurrentMap<K, V>
