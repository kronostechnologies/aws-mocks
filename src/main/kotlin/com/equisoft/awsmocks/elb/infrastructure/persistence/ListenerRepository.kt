package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.amazonaws.services.elasticloadbalancingv2.model.Listener

class ListenerRepository : BaseElbRepository<String, Listener>()
