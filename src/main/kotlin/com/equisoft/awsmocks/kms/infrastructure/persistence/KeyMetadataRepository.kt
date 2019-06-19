package com.equisoft.awsmocks.kms.infrastructure.persistence

import com.amazonaws.services.kms.model.KeyMetadata
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class KeyMetadataRepository : ConcurrentMap<String, KeyMetadata> by ConcurrentHashMap()
