package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.SecurityGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class SecurityGroupRepository : ConcurrentMap<String, SecurityGroup> by ConcurrentHashMap()
