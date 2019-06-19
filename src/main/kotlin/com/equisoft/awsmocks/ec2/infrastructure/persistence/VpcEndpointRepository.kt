package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.VpcEndpoint
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class VpcEndpointRepository : ConcurrentMap<String, VpcEndpoint> by ConcurrentHashMap()
