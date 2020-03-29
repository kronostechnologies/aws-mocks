package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.ServiceTypeDetail
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem

interface ServiceDetailMixin {
    @ListItem("serviceType")
    fun getServiceType(): List<ServiceTypeDetail>

    @ListItem("availabilityZoneSet")
    fun getAvailabilityZones(): List<String>
}
