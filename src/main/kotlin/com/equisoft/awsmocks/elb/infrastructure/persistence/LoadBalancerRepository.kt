package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer

class LoadBalancerRepository : BaseElbRepository<String, LoadBalancer>()
