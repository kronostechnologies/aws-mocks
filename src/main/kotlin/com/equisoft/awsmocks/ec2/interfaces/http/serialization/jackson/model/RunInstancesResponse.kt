package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ec2.model.GroupIdentifier
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "RunInstancesResponse")
class RunInstancesResponse(reservation: Reservation) : AmazonWebServiceResult<ResponseMetadata>() {
    val ownerId: String? = reservation.ownerId
    val reservationId: String = reservation.reservationId

    @get:ListItem("groupSet")
    val groups: List<GroupIdentifier> = reservation.groups

    @get:ListItem("instancesSet")
    val instances: List<Instance> = reservation.instances
}
