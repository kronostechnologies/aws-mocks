package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.Action
import com.amazonaws.services.elasticloadbalancingv2.model.Certificate
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface ListenerMixin {
    @ListMember
    fun getCertificates(): List<Certificate>

    @ListMember
    fun getDefaultActions(): List<Action>
}
