package com.equisoft.awsmocks.acm.infrastructure.persistence

import com.amazonaws.services.certificatemanager.model.CertificateDetail
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class CertificateRepository : ConcurrentMap<String, CertificateDetail> by ConcurrentHashMap()
