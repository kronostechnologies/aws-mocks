package com.equisoft.awsmocks.elb.interfaces.http.serialization.jackson.model

import com.amazonaws.services.elasticloadbalancingv2.model.Listener
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListMember

interface CreateListenerResultMixin {
    @ListMember
    fun getListeners(): List<Listener>
}
