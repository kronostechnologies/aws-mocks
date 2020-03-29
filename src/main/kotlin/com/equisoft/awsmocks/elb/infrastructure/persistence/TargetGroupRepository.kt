package com.equisoft.awsmocks.elb.infrastructure.persistence

import com.amazonaws.services.elasticloadbalancingv2.model.TargetGroup

class TargetGroupRepository : BaseElbRepository<String, TargetGroup>()
